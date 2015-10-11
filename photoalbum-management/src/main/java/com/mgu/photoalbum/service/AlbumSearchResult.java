package com.mgu.photoalbum.service;

import java.util.Collections;
import java.util.List;

public class AlbumSearchResult {

    private final int total;

    private final List<AlbumHit> hits;

    public AlbumSearchResult(final int total, final List<AlbumHit> hits) {
        this.total = total;
        this.hits = hits;
    }

    public int getTotal() {
        return this.total;
    }

    public int getHitCount() {
        return this.getHitCount();
    }

    public List<AlbumHit> getHits() {
        return Collections.unmodifiableList(hits);
    }
}