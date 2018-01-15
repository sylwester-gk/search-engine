package com.sgk.search.search;

import java.util.List;

/**
 * Search service interface
 */
public interface SearchService {

    /**
     * Add a document to the index
     * @param file name of indexed file
     * @param data content of the file
     */
    void index(String file, String data);

    /**
     * Search the database for the given term
     * @param term to be found
     * @return List of files containing given term
     */
    List<String> search(String term);
}
