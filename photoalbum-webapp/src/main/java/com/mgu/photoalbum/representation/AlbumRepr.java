package com.mgu.photoalbum.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AlbumRepr {

    public static class AlbumReprBuilder {

        private MetaRepr meta;
        private String title = StringUtils.EMPTY;
        private int numberOfPhotos = 0;
        private int offset = 0;
        private int pageSize = 0;
        private List<String> tags = new LinkedList<>();
        private List<PhotoShortRepr> photos = new LinkedList<>();

        public AlbumReprBuilder meta(final MetaRepr meta) {
            this.meta = meta;
            return this;
        }

        public AlbumReprBuilder title(final String title) {
            this.title = title;
            return this;
        }

        public AlbumReprBuilder numberOfPhotos(final int numberOfPhotos) {
            this.numberOfPhotos = numberOfPhotos;
            return this;
        }

        public AlbumReprBuilder offset(final int offset) {
            this.offset = offset;
            return this;
        }

        public AlbumReprBuilder pageSize(final int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public AlbumReprBuilder tag(final String tag) {
            this.tags.add(tag);
            return this;
        }

        public AlbumReprBuilder tag(final List<String> tags) {
            this.tags.addAll(tags);
            return this;
        }

        public AlbumReprBuilder photo(final PhotoShortRepr photo) {
            this.photos.add(photo);
            return this;
        }

        public AlbumReprBuilder photo(final List<PhotoShortRepr> photos) {
            this.photos.addAll(photos);
            return this;
        }

        public AlbumRepr build() {
            return new AlbumRepr(this);
        }
    }

    @JsonProperty("_meta")
    private final MetaRepr meta;

    @JsonProperty("title")
    private final String title;

    @JsonProperty("numberOfPhotos")
    private final int numberOfPhotos;

    @JsonProperty("numberOfIncludedPhotos")
    private final int numberOfIncludedPhotos;

    @JsonProperty("offset")
    private final int offset;

    @JsonProperty("pageSize")
    private final int pageSize;

    @JsonProperty("tags")
    private final List<String> tags;

    @JsonProperty("photos")
    private final List<PhotoShortRepr> photos;

    private AlbumRepr(final AlbumReprBuilder builder) {
        this.meta = builder.meta;
        this.title = builder.title;
        this.numberOfPhotos = builder.numberOfPhotos;
        this.numberOfIncludedPhotos = builder.photos.size();
        this.offset = builder.offset;
        this.pageSize = builder.pageSize;
        this.tags = builder.tags;
        this.photos = builder.photos;
    }

    public MetaRepr getMeta() {
        return meta;
    }

    public String getTitle() {
        return title;
    }

    public int getNumberOfPhotos() {
        return numberOfPhotos;
    }

    public int getNumberOfIncludedPhotos() {
        return numberOfIncludedPhotos;
    }

    public int getOffset() {
        return offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public List<PhotoShortRepr> getPhotos() {
        return Collections.unmodifiableList(photos);
    }

    public static AlbumReprBuilder create() {
        return new AlbumReprBuilder();
    }
}