package com.mgu.photoalbum.webapp.representation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateAlbumRepr {

    private final String albumName;

    @JsonCreator
    public CreateAlbumRepr(@JsonProperty("albumName") String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumName() {
        return albumName;
    }
}