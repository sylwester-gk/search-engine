package com.sgk.search.config;

import com.sgk.search.loader.FileLoader;
import com.sgk.search.loader.Loader;
import com.sgk.search.search.BasicSearchService;
import com.findwise.SearchEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchServiceConfig {

    @Bean
    public Loader loader() {
        return new FileLoader();
    }

    @Bean
    public SearchEngine searchService(Loader loader) {
        SearchEngine searchService = new BasicSearchService();
        loader.getDocuments().forEach(d -> searchService.indexDocument(d.getName(), d.getData()));
        return searchService;
    }
}
