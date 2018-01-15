package com.sgk.search.search;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SearchService implementation that uses inverted index to store data
 * and TF*IDF for term weight calculation
 */
@Service
public class BasicSearchService implements SearchService {

    @Override
    public void index(String file, String data) {

    }

    @Override
    public List<String> search(String term) {
        return null;
    }
}
