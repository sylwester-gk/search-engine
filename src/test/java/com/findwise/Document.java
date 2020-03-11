package com.findwise;

public class Document {
    private String name;
    private String data;

    public Document(String name, String data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }
}