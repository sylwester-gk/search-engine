package com.sgk.search.loader;

import com.sgk.search.model.Document;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Slf4j
public class FileLoader implements Loader {

    @Value("${data.path}")
    private String dataPath;

    @Override
    public long getTotalDocumentCount() {
        try {
            return Files.list(Paths.get(dataPath)).count();
        } catch (IOException e) {
            log.error("Cannot access directory {}", dataPath);
            return 0;
        }
    }

    @Override
    public Stream<Document> getDocuments() {
        try {
            return Files.list(Paths.get(dataPath))
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
                    });
        } catch (IOException e) {
            log.error("Cannot access directory {}", dataPath);
            return Stream.empty();
        }
    }
}
