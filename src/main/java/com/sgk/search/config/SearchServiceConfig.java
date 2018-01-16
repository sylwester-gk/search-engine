package com.sgk.search.config;

import com.sgk.search.loader.FileLoader;
import com.sgk.search.loader.Loader;
import com.sgk.search.search.BasicSearchService;
import com.sgk.search.search.SearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchServiceConfig {

    @Bean
    public Loader loader() {
        return new FileLoader();
    }

    @Bean
    public SearchService searchService(Loader loader){
        SearchService searchService = new BasicSearchService();
        searchService.indexAll(loader);
        return searchService;
    }
}
