package com.sgk.search;


import com.sgk.search.search.BasicSearchService;
import com.sgk.search.search.SearchService;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SearchServiceTest {

    private static final Map<String, String> data = new HashMap<String, String>() {
        {
            put("Document 1", "the brown fox jumped over the brown dog");
            put("Document 2", "the lazy brown dog sat in the corner");
            put("Document 3", "the red fox bit the lazy dog");
        }
    };

    private SearchService searchService;

    @Before
    public void initSearchService() {
        searchService = new BasicSearchService();
        data.forEach((filename,data) -> searchService.index(filename, data));
    }

    @Test
    public void searchSingleWordTest() {
        List<String> brown = searchService.search("brown");
        assertTrue(brown.contains("Document 1"));
        assertTrue(brown.contains("Document 2"));
        assertFalse(brown.contains("Document 3"));
    }

}
