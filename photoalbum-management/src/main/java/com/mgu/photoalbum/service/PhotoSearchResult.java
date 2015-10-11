package com.mgu.photoalbum.service;

import com.mgu.photoalbum.domain.Photo;

import java.util.Collections;
import java.util.List;

public class PhotoSearchResult {

    private final PhotoSearchRequest searchRequest;

    private final int total;

    private final List<Photo> hits;

    public PhotoSearchResult(final PhotoSearchRequest searchRequest, final int total, final List<Photo> hits) {
        this.searchRequest = searchRequest;
        this.total = total;
        this.hits = hits;
    }

    public int getTotal() {
        return total;
    }

    public int getHitCount() {
        return this.hits.size();
    }

    public List<Photo> getHits() {
        return Collections.unmodifiableList(hits);
    }

    public PhotoSearchRequest getSearchRequest() {
        return this.searchRequest;
    }
}