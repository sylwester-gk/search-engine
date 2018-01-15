package com.sgk.search.loader;


import com.sgk.search.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@Component
public class Loader {

    @Value("${data.path}")
    private String dataPath;

    private final SearchService searchService;

    @Autowired
    public Loader(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostConstruct
    public void loadData() {
        File folder = new File(dataPath);

    }
}
