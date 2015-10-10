package com.mgu.photoalbum.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumShortRepr {

    public static class AlbumShortReprBuilder {

        private Map<String, LinkRepr> namedLinks = new HashMap<>();
        private String albumId = StringUtils.EMPTY;
        private String title = StringUtils.EMPTY;
        private int numberOfPhotos = 0;

        public AlbumShortReprBuilder id(final String albumId) {
            this.albumId = albumId;
            return this;
        }

        public AlbumShortReprBuilder title(final String title) {
            this.title = title;
            return this;
        }

        public AlbumShortReprBuilder numberOfPhotos(final int numberOfPhotos) {
            this.numberOfPhotos = numberOfPhotos;
            return this;
        }

        public AlbumShortReprBuilder link(final LinkRepr link) {
            this.namedLinks.put(link.getRelation(), link);
            return this;
        }

        public AlbumShortRepr build() {
            return new AlbumShortRepr(this);
        }
    }

    @JsonProperty("albumId")
    private final String albumId;

    @JsonProperty("title")
    private final String title;

    @JsonProperty("numberOfPhotos")
    private final int numberOfPhotos;

    @JsonProperty("links")
    private final Map<String, LinkRepr> namedLinks;

    private AlbumShortRepr(final AlbumShortReprBuilder builder) {
        this.albumId = builder.albumId;
        this.title = builder.title;
        this.numberOfPhotos = builder.numberOfPhotos;
        this.namedLinks = builder.namedLinks;
    }

    public String getAlbumId() {
        return this.albumId;
    }

    public String getTitle() {
        return title;
    }

    public int getNumberOfPhotos() {
        return numberOfPhotos;
    }

    public Map<String, LinkRepr> getNamedLinks() {
        return Collections.unmodifiableMap(namedLinks);
    }

    public static AlbumShortReprBuilder create() {
        return new AlbumShortReprBuilder();
    }
}