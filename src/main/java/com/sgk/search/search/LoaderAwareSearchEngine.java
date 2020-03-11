package com.sgk.search.search;

import com.findwise.SearchEngine;
import com.sgk.search.loader.Loader;

public interface LoaderAwareSearchEngine extends SearchEngine {
    /**
     * Index all documents from source
     *
     * @param source of documents
     */
    void indexAll(Loader source);
}
