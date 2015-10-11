package com.mgu.photoalbum.service;

import com.mgu.photoalbum.domain.Album;

public class AlbumHit {

    private final Album album;

    private final int numberOfPhotos;

    private final String coverPhotoId;

    public AlbumHit(final Album album, final int numberOfPhotos, final String coverPhotoId) {
        this.album = album;
        this.numberOfPhotos = numberOfPhotos;
        this.coverPhotoId = coverPhotoId;
    }

    public Album getAlbum() {
        return album;
    }

    public int getNumberOfPhotos() {
        return numberOfPhotos;
    }

    public String getCoverPhotoId() {
        return this.coverPhotoId;
    }
}