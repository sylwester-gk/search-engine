package com.sgk.search.search;

import com.findwise.IndexEntry;
import com.sgk.search.loader.Loader;
import com.sgk.search.model.IndexEntryImpl;
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
    private Map<String, List<IndexEntry>> tfidfIndex = new HashMap<>();


    @Override
    public void indexAll(Loader source) {
        // load documents and calculate term frequencies
        source.getDocuments().forEach(d -> indexDocument(d.getName(), d.getData()));
        // calculate inverse document frequencies
        buildIndex();
    }

    /**
     * Add a document to the index
     *
     * @param id name of indexed document
     * @param content content of the document
     */
    @Override
    public void indexDocument(String id, String content) {

        List<String> tokens = Arrays.asList(content.split(TOKENIZER_REGEX));

        Set<String> toProcess = new HashSet<>(tokens);

        // we need one entry for each term
        toProcess.forEach(term -> {
            List<IndexEntry> entryList = tfidfIndex.computeIfAbsent(term, key -> new LinkedList<>());
            IndexEntryImpl entry = new IndexEntryImpl();
            entry.setId(id);
            entryList.add(entry);
        });

        tfidfIndex.keySet().forEach(term -> {
            double termFrequency = (double) Collections.frequency(tokens, term) / tokens.size();
            Map<String, Double> map = tfPerFile.computeIfAbsent(term, key -> new HashMap<>());
            map.put(id, termFrequency);
        });

        totalNumberOfIndexedDocuments++;

        log.info("Indexed document: {}", id);
    }

    /**
     * Compute index for all loaded documents
     */
    private void buildIndex() {
        tfPerFile.forEach((term, termFrequenciesPerDocument) -> {
            double idf = Math.log((double) totalNumberOfIndexedDocuments / 1 + tfidfIndex.get(term).size());

            termFrequenciesPerDocument.forEach((docName, termFrequency) -> {
                        double tfidf = termFrequency * idf;
                        log.debug("tf-idf doc {} term {} tf-idf {}", docName, term, tfidf);

                        List<IndexEntry> tfIdfWeights = tfidfIndex.get(term);

                        tfIdfWeights.stream()
                                .filter(entry -> docName.equals(entry.getId()))
                                .findFirst()
                                .ifPresent(entry ->
                                        entry.setScore(tfidf)
                                );
                    }
            );

            // Sort loaded data
            tfidfIndex.get(term).sort(Comparator.comparing(IndexEntry::getScore).reversed());
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
    public List<IndexEntry> search(String term) {
        List<IndexEntry> indexEntries = tfidfIndex.getOrDefault(term, Collections.emptyList());
        return indexEntries.stream()
                .filter(e -> e.getScore() > 0)
                .collect(Collectors.toList());
    }

}
