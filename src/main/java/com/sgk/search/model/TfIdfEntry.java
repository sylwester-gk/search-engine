package com.sgk.search.model;

import lombok.Data;

@Data
public class TfIdfEntry {
    private String documentName;
    private double tfIdfWeight;

//    @Override
//    public String toString() {
//        return "doc " + name + ", score" + tfIdfWeight;
//    }
}