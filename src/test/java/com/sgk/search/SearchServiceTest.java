package com.sgk.search;


import com.sgk.search.loader.Loader;
import com.sgk.search.search.BasicSearchService;
import com.sgk.search.search.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class SearchServiceTest {

    private static final Map<String, String> corpus = new HashMap<String, String>() {
        {
            put("Document 1", "the brown fox jumped over the brown dog");
            put("Document 2", "the lazy brown dog sat in the corner");
            put("Document 3", "the red fox bit the lazy dog");
        }
    };

    private SearchService searchService;

    private Loader loader;

    @Before
    public void initSearchService() {
//        searchService = new BasicSearchService();

        Loader mock = mock(Loader.class);
        //when(mock.getDocuments())
//        corpus.forEach((name, data) -> searchService.processDocument(name, data, corpus.size()));
    }

    @Test
    public void searchSingleWordTest() {
        List<String> brown = searchService.search("brown");
        assertTrue(brown.contains("Document 1"));
        assertTrue(brown.contains("Document 2"));
        assertFalse(brown.contains("Document 3"));
    }

//    @Test
//    public void tokenizerTest() {
//        String corpus = "the brown, fox jumped over.the brown dog    some thing   .";
//        String[] words = corpus.split("\\W+");
//    }

}
