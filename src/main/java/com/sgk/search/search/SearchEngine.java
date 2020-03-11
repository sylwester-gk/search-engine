package com.sgk.search.search;

import java.util.List;

/**
 * Search service interface
 */
public interface SearchEngine {

    /**
     * Add a document to the index
     *
     * @param documentName name of indexed document
     * @param content content of the document
     */
    void indexDocument(String documentName, String content);

    /**
     * Search the index for the given term
     *
     * @param term to be found
     *
     * @return List of files containing given term sorted by TF-IDF
     */
    List<String> search(String term);
}
