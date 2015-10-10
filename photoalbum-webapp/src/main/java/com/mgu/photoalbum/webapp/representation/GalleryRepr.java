package com.mgu.photoalbum.webapp.representation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

public class GalleryRepr {

    public static class GalleryReprBuilder {

        private MetaRepr meta = MetaRepr.create().build();
        private List<AlbumShortRepr> albums = new LinkedList<>();

        public GalleryReprBuilder meta(final MetaRepr meta) {
            this.meta = meta;
            return this;
        }

        public GalleryReprBuilder album(final AlbumShortRepr album) {
            this.albums.add(album);
            return this;
        }

        public GalleryReprBuilder album(final List<AlbumShortRepr> albums) {
            this.albums.addAll(albums);
            return this;
        }

        public GalleryRepr build() {
            return new GalleryRepr(this);
        }
    }

    @JsonProperty("_meta")
    private MetaRepr meta;

    @JsonProperty("albums")
    private List<AlbumShortRepr> albums = new LinkedList<>();

    private GalleryRepr(final GalleryReprBuilder builder) {
        this.meta = builder.meta;
        this.albums = builder.albums;
    }

    public static GalleryReprBuilder create() {
        return new GalleryReprBuilder();
    }
}