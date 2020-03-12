package com.findwise;

import com.sgk.search.search.BasicSearchService;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import static java.lang.String.format;

public class SearchTest_long extends AbstractSearchTest {

    public static final String DATA_PATH = "sample-data-2";
    public static final String BEYOND_TERMS_TXT = "Beyond-terms.txt";
    public static final String JUSTIFICATION_TXT = "justification.txt";
    public static final String MOTIVATIONS_TXT = "motivations.txt";
    public static final String ANATOMY_OF_A_MICROSERVICE = "Anatomy of a Microservice";
    public static final String REACT_NATIVE_VS_FLUTTER = "React Native vs Flutter";
    public static final String DEV_OPS_CULTURE = "DevOps culture";

    @BeforeClass
    public static void initialize() throws IOException {
        searchEngine = new BasicSearchService();

        Files.list(Paths.get(DATA_PATH))
                .filter(Files::isRegularFile)
                .map(path -> {
                    String filename = path.getFileName().toString();
                    String data = "";
                    try {
                        data = Files.readString(path);
                    } catch (IOException e) {
                        System.out.println(format("Cannot read file {}", filename));
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

        String[] expected = {BEYOND_TERMS_TXT, JUSTIFICATION_TXT, MOTIVATIONS_TXT,
                ANATOMY_OF_A_MICROSERVICE, REACT_NATIVE_VS_FLUTTER, DEV_OPS_CULTURE};

        assertContents(result, expected);
    }


}
