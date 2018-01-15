package com.sgk.search.loader;


import com.sgk.search.loader.model.Document;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Slf4j
@Component
public class Loader {

    @Value("${data.path}")
    private String dataPath;

    public long getTotalDocumentCount() {
        try {
            return Files.list(Paths.get(dataPath)).count();
        } catch (IOException e) {
            log.error("Cannot access directory {}", dataPath);
            return 0;
        }
    }

    public Stream<Document> getDocuments()  {
        try {
            return Files.list(Paths.get(dataPath))
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        Document d = new Document();
                        String filename = path.getFileName().toString();
                        d.setName(filename);
                        try {
                            String data = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                            d.setData(data);
                        } catch (IOException e) {
                            log.error("Cannot read file {}", filename);
                        }
                        return d;
                    });
        } catch (IOException e) {
            log.error("Cannot access directory {}", dataPath);
            return Stream.empty();
        }
    }
}
