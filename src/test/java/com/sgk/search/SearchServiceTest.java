package com.sgk.search;

import com.sgk.search.loader.Loader;
import com.sgk.search.model.Document;
import com.sgk.search.search.BasicSearchService;
import com.sgk.search.search.LoaderAwareSearchEngine;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import static com.sgk.search.search.BasicSearchService.TOKENIZER_REGEX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
public class SearchServiceTest {

    private static final List<Document> CORPUS = new ArrayList<Document>() {
        {
            add(new Document("Document 1", "the brown fox jumped over the brown dog"));
            add(new Document("Document 2", "the lazy brown dog sat in the corner"));
            add(new Document("Document 3", "the red fox bit the lazy dog"));
        }
    };

    private static Loader loader;

    private LoaderAwareSearchEngine searchService;


    @BeforeClass
    public static void initializeLoader() {
        loader = new Loader() {

            @Override
            public long getTotalDocumentCount() {
                return CORPUS.size();
            }

            @Override
            public Stream<Document> getDocuments() {
                return CORPUS.stream();
            }
        };
    }

    @Before
    public void initSearchService() {
        searchService = new BasicSearchService();
        searchService.indexAll(loader);
    }

    @Test
    public void searchSingleWordTest_brown() {
        List<String> result = searchService.search("brown");

        String[] expected = {"Document 1", "Document 2"};
        assertEquals(expected.length, result.size());

        for (int i = 0; i < result.size(); i++) {
            assertEquals(expected[i], result.get(i));
        }
    }

    @Test
    public void searchSingleWordTest_fox() {
        List<String> result = searchService.search("fox");

        String[] expected = {"Document 3", "Document 1"};
        assertEquals(expected.length, result.size());

        for (int i = 0; i < result.size(); i++) {
            assertEquals(expected[i], result.get(i));
        }
    }

    @Test
    public void searchSingleWordTest_the() {
        List<String> result = searchService.search("the");

        String[] expected = {"Document 3", "Document 1", "Document 2"};
        assertEquals(expected.length, result.size());

        for (int i = 0; i < result.size(); i++) {
            assertEquals(expected[i], result.get(i));
        }
    }

    @Test
    public void tokenizerRegexTest() {
        String document = "the brown, fox //jumped over.the \nbrown; dog    some !@#$%^&thing   .";
        String[] words = document.split(TOKENIZER_REGEX);
        String[] expected = {"the", "brown", "fox", "jumped", "over", "the", "brown", "dog", "some", "thing"};

        for (int i = 0; i < expected.length; i++) {
            assertTrue(words[i].equals(expected[i]));
        }

    }

}
