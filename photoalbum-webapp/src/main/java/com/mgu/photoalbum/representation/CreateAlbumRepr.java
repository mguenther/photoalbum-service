package com.mgu.photoalbum.representation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateAlbumRepr {

    @JsonProperty("albumName")
    private String albumName;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
}