package com.sgk.search.search;

import java.util.List;

/**
 * Search service interface
 */
public interface SearchService {

    /**
     * Add a document to the index
     * @param name name of indexed document
     * @param data content of the document
     */
    void processDocument(String name, String data);

    /**
     * Search the database for the given term
     * @param term to be found
     * @return List of files containing given term
     */
    List<String> search(String term);
}
