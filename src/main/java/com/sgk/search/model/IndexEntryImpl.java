package com.sgk.search.model;

import com.findwise.IndexEntry;
import lombok.Data;

@Data
public class IndexEntryImpl implements IndexEntry {
    private String id;
    private double score;
}