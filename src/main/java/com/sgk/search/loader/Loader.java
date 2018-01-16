package com.sgk.search.loader;

import com.sgk.search.loader.model.Document;

import java.util.stream.Stream;

/**
 * Represents a strategy for loading data from various sources
 */
public interface Loader {
    /**
     *
     * @return total number of documents that are available for the Loader
     */
    long getTotalDocumentCount();

    /**
     * This method reads source data and converts it to a stream of abstract Documents representing
     * a piece of data from given 'corpus'
     *
     * @return a stream of Documents
     */
    Stream<Document> getDocuments();
}
