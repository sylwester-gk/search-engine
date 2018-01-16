package com.sgk.search.search;

import com.sgk.search.loader.Loader;

import java.util.List;

/**
 * Search service interface
 */
public interface SearchService {

    /**
     * Index all documents from source
     * @param source of documents
     */
    void indexAll(Loader source);


    /**
     * Search the database for the given term
     * @param term to be found
     * @return List of files containing given term
     */
    List<String> search(String term);
}
