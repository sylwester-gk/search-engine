package com.findwise;

import com.sgk.search.search.BasicSearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SearchTest extends AbstractSearchTest {
    protected static List<Document> CORPUS = new ArrayList<Document>() {
        {
            add(new Document("Document 1", "the brown fox jumped over the brown dog"));
            add(new Document("Document 2", "the lazy brown dog sat in the corner"));
            add(new Document("Document 3", "the red fox bit the lazy dog"));
        }
    };

    @BeforeClass
    public static void initialize() {
        searchEngine = new BasicSearchService();
        // load documents and calculate term frequencies
        CORPUS.forEach(d -> searchEngine.indexDocument(d.getName(), d.getData()));
    }

    @Test
    public void searchSingleWordTest_brown() {
        List<IndexEntry> result = searchEngine.search("brown");

        String[] expected = {"Document 1", "Document 2"};

        assertContents(result, expected);
    }

    @Test
    public void searchSingleWordTest_fox() {
        List<IndexEntry> result = searchEngine.search("fox");

        String[] expected = {"Document 3", "Document 1"};

        assertContents(result, expected);
    }

    @Test
    public void searchSingleWordTest_the() {
        List<IndexEntry> result = searchEngine.search("the");

        String[] expected = {"Document 3", "Document 1", "Document 2"};

        assertContents(result, expected);
    }
}
