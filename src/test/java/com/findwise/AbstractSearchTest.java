package com.findwise;

import java.util.List;
import static org.junit.Assert.assertEquals;

public abstract class AbstractSearchTest {

    protected static SearchEngine searchEngine;

    protected void assertContents(List<IndexEntry> result, String[] expected) {
        assertEquals(expected.length, result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(expected[i], result.get(i).getId());
        }
    }

}
