package com.sgk.search.search;

import com.sgk.search.loader.Loader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * SearchService implementation that uses inverted processDocument to store data
 * and TF-IDF for term weight calculation
 */
@Slf4j
@Service
public class BasicSearchService implements SearchService {

    private final Loader source;
    private long totalNumberOfIndexedDocuments;

    /**
     * term -> [filename]
     */
    private Map<String, Set<String>> index = new HashMap<>();

    /**
     * term -> [file -> tf]
     */
    private Map<String, Map<String, Double>> tfMatrix = new HashMap<>();


    /**
     * term -> [file -> tfidf]
     */
    private Map<String, Map<String, Double>> tfidf = new HashMap<>();

    @Autowired
    public BasicSearchService(Loader source) {
        this.source = source;
    }

    @PostConstruct
    public void indexAll() {
        totalNumberOfIndexedDocuments = source.getTotalDocumentCount();
        // load documents and calculate term frequencies
        source.getDocuments().forEach(d -> processDocument(d.getName(), d.getData()));
        // calculate inverse document frequencies
        buildIndex();
    }

    @Override
    public void processDocument(String name, String data) {
        List<String> tokens = Arrays.asList(data.split("\\W+"));

        Set<String> toProcess = new HashSet<>(tokens);

        for (String term : toProcess) {
            if (!index.keySet().contains(term)) index.put(term, new HashSet<>());
            Set<String> set = index.get(term);
            set.add(name);
        }

        for (String term : index.keySet()) {
            double termFrequency = (double) Collections.frequency(tokens, term) / tokens.size();
            if (!tfMatrix.keySet().contains(term)) tfMatrix.put(term, new HashMap<>());
            Map<String, Double> map = tfMatrix.get(term);
            map.put(name, termFrequency);
        }

        log.info("Indexed: {}", name);
    }

    private void buildIndex() {
        tfMatrix.forEach((term, valueMap) -> {
            double idf = Math.log((double) totalNumberOfIndexedDocuments / index.get(term).size());

            valueMap.forEach((docName, termFrequency) -> {
                        double tfidf = termFrequency * idf;
                        log.info("tf-idf doc {} term {} tf-idf {}", docName, term, tfidf);
                    }
            );
        });
    }


    @Override
    public List<String> search(String term) {
        return null;
    }


    private float calculateTfidf() {
        return 0.0f;
    }
}
