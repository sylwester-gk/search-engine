package com.findwise;

import java.util.List;

/**
 * Search service interface
 */
public interface SearchEngine {

    /**
     * Add a document to the index
     *
     * @param id name of indexed document
     * @param content content of the document
     */
    void indexDocument(String id, String content);

    /**
     * Search the index for the given term
     *
     * @param term to be found
     *
     * @return List of search results containing given term sorted by TF-IDF
     */
    List<IndexEntry> search(String term);
}
