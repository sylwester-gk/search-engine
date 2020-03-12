package com.sgk.search.controller;

import com.findwise.IndexEntry;
import com.findwise.SearchEngine;
import com.sgk.search.model.Document;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@RestController
public class SearchController {

    @Value("${data.path}")
    private String dataPath;

    private final SearchEngine searchService;

    @Autowired
    public SearchController(SearchEngine searchService) {
        this.searchService = searchService;
    }

    @GetMapping(path = "/search={query}", produces = "application/json")
    public @ResponseBody
    List<IndexEntry> search(@PathVariable String query) {
        return searchService.search(query);
    }

    @PostMapping(path = "/index", consumes = "application/json", produces = "application/json")
    public void index(@RequestBody Document doc) {
        saveFile(doc);
        searchService.indexDocument(doc.getName(), doc.getData());
    }

    private void saveFile(Document doc) {
        Path path = Paths.get(dataPath + "/" + doc.getName());
        try {
            Files.write(path, doc.getData().getBytes());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
