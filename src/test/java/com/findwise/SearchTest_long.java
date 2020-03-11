package com.findwise;

import com.sgk.search.search.BasicSearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class SearchTest_long extends AbstractSearchTest {

    public static final String DATA_PATH = "sample-data-2";

    @BeforeClass
    public static void initialize() throws IOException {
        searchEngine = new BasicSearchService();

        Files.list(Paths.get(DATA_PATH))
                .filter(Files::isRegularFile)
                .map(path -> {
                    String filename = path.getFileName().toString();
                    String data = "";
                    try {
                        data = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                    } catch (IOException e) {
                        log.error("Cannot read file {}", filename);
                    }
                    return new Document(filename, data);
                })
                .forEach(d -> searchEngine.indexDocument(d.getName(), d.getData()));
    }

    /************************
     *        TESTS         *
     ************************/
    @Test
    public void searchSingleWordTest_brown() {
        List<IndexEntry> result = searchEngine.search("made");

        String[] expected = {"Document 1", "Document 2"};

        assertContents(result, expected);
    }


}
