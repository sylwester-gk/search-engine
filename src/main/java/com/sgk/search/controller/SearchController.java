package com.sgk.search.controller;

import com.sgk.search.search.SearchEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
public class SearchController {

    private final SearchEngine searchService;

    @Autowired
    public SearchController(SearchEngine searchService) {
        this.searchService = searchService;
    }

    @GetMapping(value = "/search={query}", produces = "application/json")
    public @ResponseBody
    List<String> search(@PathVariable String query) {
        return searchService.search(query);
    }
}
