package com.sgk.search.loader;

import com.sgk.search.loader.model.Document;

import java.util.stream.Stream;

public interface Loader {
    long getTotalDocumentCount();
    Stream<Document> getDocuments();
}
