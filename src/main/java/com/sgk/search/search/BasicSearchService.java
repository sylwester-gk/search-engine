package com.sgk.search.search;

import com.sgk.search.loader.Loader;
import com.sgk.search.model.TfIdfEntry;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SearchService implementation that uses inverted processDocument to store data
 * and TF-IDF for term weight calculation
 */
@Slf4j
public class BasicSearchService implements LoaderAwareSearchEngine {

    public static final String TOKENIZER_REGEX = "\\W+";
    private long totalNumberOfIndexedDocuments;

    /**
     * term -> [file -> tf]
     */
    private Map<String, Map<String, Double>> tfPerFile = new HashMap<>();

    /**
     * term -> [file -> tfidf weight]
     */
    private Map<String, List<TfIdfEntry>> tfidfIndex = new HashMap<>();


    @Override
    public void indexAll(Loader source) {
        totalNumberOfIndexedDocuments = source.getTotalDocumentCount();
        // load documents and calculate term frequencies
        source.getDocuments().forEach(d -> indexDocument(d.getName(), d.getData()));
        // calculate inverse document frequencies
        buildIndex();
    }

    /**
     * Add a document to the index
     *
     * @param documentName name of indexed document
     * @param content content of the document
     */
    @Override
    public void indexDocument(String documentName, String content) {
        List<String> tokens = Arrays.asList(content.split(TOKENIZER_REGEX));

        Set<String> toProcess = new HashSet<>(tokens);

        // we need one entry for each term
        toProcess.forEach(term -> {
            List<TfIdfEntry> entryList = tfidfIndex.computeIfAbsent(term, key -> new LinkedList<>());
            TfIdfEntry entry = new TfIdfEntry();
            entry.setDocumentName(documentName);
            entryList.add(entry);
        });

        tfidfIndex.keySet().forEach(term -> {
            double termFrequency = (double) Collections.frequency(tokens, term) / tokens.size();
            Map<String, Double> map = tfPerFile.computeIfAbsent(term, key -> new HashMap<>());
            map.put(documentName, termFrequency);
        });

        log.info("Indexed document: {}", documentName);
    }

    /**
     * Compute index for all loaded documents
     */
    private void buildIndex() {
        tfPerFile.forEach((term, termFrequenciesPerDocument) -> {
            double idf = Math.log((double) totalNumberOfIndexedDocuments / 1 + tfidfIndex.get(term).size());

            termFrequenciesPerDocument.forEach((docName, termFrequency) -> {
                        double tfidfValue = termFrequency * idf;
                        log.debug("tf-idf doc {} term {} tf-idf {}", docName, term, tfidfValue);

                        List<TfIdfEntry> tfidfWeights = tfidfIndex.get(term);

                        tfidfWeights.stream()
                                .filter(entry -> docName.equals(entry.getDocumentName()))
                                .findFirst()
                                .ifPresent(entry ->
                                        entry.setTfIdfWeight(tfidfValue)
                                );
                    }
            );

            // Sort loaded data
            tfidfIndex.get(term).sort(Comparator.comparing(TfIdfEntry::getTfIdfWeight).reversed());
        });
    }

    /**
     * Search for given term and return a list of documents containing it, ordered by TF-IDF weights.
     *
     * @param term to be found
     *
     * @return list of document names
     */
    @Override
    public List<String> search(String term) {
        List<TfIdfEntry> tfidfEntries = tfidfIndex.getOrDefault(term, Collections.emptyList());
        return tfidfEntries.stream()
                .filter(e -> e.getTfIdfWeight() > 0)
                .map(TfIdfEntry::getDocumentName)
                .collect(Collectors.toList());
    }

}
