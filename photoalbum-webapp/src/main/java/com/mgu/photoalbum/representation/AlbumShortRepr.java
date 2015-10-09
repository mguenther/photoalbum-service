package com.mgu.photoalbum.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AlbumShortRepr {

    public static class AlbumShortReprBuilder {

        private String title = StringUtils.EMPTY;
        private int numberOfPhotos = 0;
        private List<LinkRepr> links = new LinkedList<>();

        public AlbumShortReprBuilder title(final String title) {
            this.title = title;
            return this;
        }

        public AlbumShortReprBuilder numberOfPhotos(final int numberOfPhotos) {
            this.numberOfPhotos = numberOfPhotos;
            return this;
        }

        public AlbumShortReprBuilder link(final LinkRepr link) {
            this.links.add(link);
            return this;
        }

        public AlbumShortRepr build() {
            return new AlbumShortRepr(this);
        }
    }

    @JsonProperty("title")
    private final String title;

    @JsonProperty("numberOfPhotos")
    private final int numberOfPhotos;

    @JsonProperty("links")
    private final List<LinkRepr> links;

    private AlbumShortRepr(final AlbumShortReprBuilder builder) {
        this.title = builder.title;
        this.numberOfPhotos = builder.numberOfPhotos;
        this.links = builder.links;
    }

    public String getTitle() {
        return title;
    }

    public int getNumberOfPhotos() {
        return numberOfPhotos;
    }

    public List<LinkRepr> getLinks() {
        return Collections.unmodifiableList(links);
    }

    public static AlbumShortReprBuilder create() {
        return new AlbumShortReprBuilder();
    }
}