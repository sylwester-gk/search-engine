package com.sgk.search.search;

import com.sgk.search.loader.Loader;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SearchService implementation that uses inverted processDocument to store data
 * and TF-IDF for term weight calculation
 */
@Slf4j
public class BasicSearchService implements SearchService {

    private long totalNumberOfIndexedDocuments;

    /**
     * term -> [file]
     */
    private Map<String, Set<String>> index = new HashMap<>();

    /**
     * term -> [file -> tf]
     */
    private Map<String, Map<String, Double>> tfMatrix = new HashMap<>();


    /**
     * term -> [file -> tfidf weight]
     */
    private Map<String, List<TfidfEntry>> tfidfIndex = new HashMap<>();

    @Data
    private class TfidfEntry {
        private String name;
        private double tfidfWeight;
    }

    @Override
    public void indexAll(Loader source) {
        totalNumberOfIndexedDocuments = source.getTotalDocumentCount();
        // load documents and calculate term frequencies
        source.getDocuments().forEach(d -> processDocument(d.getName(), d.getData()));
        // calculate inverse document frequencies
        buildIndex();
    }

    /**
     * Add a document to the index
     * @param name name of indexed document
     * @param data content of the document
     */
    private void processDocument(String name, String data) {
        List<String> tokens = Arrays.asList(data.split("\\W+"));

        Set<String> toProcess = new HashSet<>(tokens);

        toProcess.forEach(term -> {
            if (!index.keySet().contains(term)) index.put(term, new HashSet<>());
            Set<String> set = index.get(term);
            set.add(name);
        });

        index.keySet().forEach(term -> {
            double termFrequency = (double) Collections.frequency(tokens, term) / tokens.size();
            if (!tfMatrix.keySet().contains(term)) tfMatrix.put(term, new HashMap<>());
            Map<String, Double> map = tfMatrix.get(term);
            map.put(name, termFrequency);
        });

        log.info("Indexed document: {}", name);
    }

    private void buildIndex() {
        tfMatrix.forEach((term, termFrequenciesPerDocument) -> {
            double idf = Math.log((double) totalNumberOfIndexedDocuments / index.get(term).size());

            termFrequenciesPerDocument.forEach((docName, termFrequency) -> {
                        double tfidfValue = termFrequency * idf;
                        log.debug("tf-idf doc {} term {} tf-idf {}", docName, term, tfidfValue);

                        List<TfidfEntry> tfidfWeights = tfidfIndex.computeIfAbsent(term, keyTerm -> new LinkedList<>());

                        TfidfEntry entry = new TfidfEntry();
                        entry.setName(docName);
                        entry.setTfidfWeight(tfidfValue);
                        tfidfWeights.add(entry);
                    }
            );

            // Sort loaded data
            tfidfIndex.get(term).sort(Comparator.comparing(TfidfEntry::getTfidfWeight).reversed());
        });
    }


    @Override
    public List<String> search(String term) {
        List<TfidfEntry> tfidfEntries = tfidfIndex.getOrDefault(term, Collections.emptyList());
        return tfidfEntries.stream()
                .filter(e -> e.getTfidfWeight() > 0)
                .map(TfidfEntry::getName)
                .collect(Collectors.toList());
    }
}
