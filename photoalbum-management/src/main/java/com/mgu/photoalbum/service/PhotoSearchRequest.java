package com.mgu.photoalbum.service;

import com.google.common.base.Optional;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Container for photo-related search parameters. Also applies input parameter sanitizing.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class PhotoSearchRequest {

    private static final int MIN_OFFSET = 0;

    private static final int MAX_PAGE_SIZE = 100;

    private static final int DEFAULT_OFFSET = MIN_OFFSET;

    private static final int DEFAULT_PAGE_SIZE = 10;

    private static final List<String> DEFAULT_FILTER_BY_TAGS = new ArrayList<>();

    public static class PhotoSearchRequestBuilder {

        private String albumId = StringUtils.EMPTY;
        private List<String> tags = new LinkedList<>();
        private int offset = DEFAULT_OFFSET;
        private int pageSize = DEFAULT_PAGE_SIZE;

        public PhotoSearchRequestBuilder albumId(final String albumId) {
            this.albumId = albumId;
            return this;
        }

        public PhotoSearchRequestBuilder tags(final List<String> tags) {
            this.tags.addAll(tags);
            return this;
        }

        public PhotoSearchRequestBuilder tags(final Optional<List<String>> optionalTags) {
            this.tags.addAll(optionalTags.or(DEFAULT_FILTER_BY_TAGS));
            return this;
        }

        public PhotoSearchRequestBuilder offset(final int offset) {
            this.offset = offset;
            return this;
        }

        public PhotoSearchRequestBuilder offset(final Optional<Integer> optionalOffset) {
            this.offset = optionalOffset
                    .transform(wrappedOffset -> max(DEFAULT_OFFSET, wrappedOffset))
                    .or(DEFAULT_OFFSET);
            return this;
        }

        public PhotoSearchRequestBuilder pageSize(final int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public PhotoSearchRequestBuilder pageSize(final Optional<Integer> optionalPageSize) {
            this.pageSize = optionalPageSize
                    .transform(wrappedPageSize -> min(max(wrappedPageSize, 0), MAX_PAGE_SIZE))
                    .or(DEFAULT_PAGE_SIZE);
            return this;
        }

        public PhotoSearchRequest build() {
            return new PhotoSearchRequest(this);
        }
    }

    private final String albumId;

    private final List<String> tags;

    private final int offset;

    private final int pageSize;

    private PhotoSearchRequest(final PhotoSearchRequestBuilder builder) {
        this.albumId = builder.albumId;
        this.tags = builder.tags;
        this.offset = builder.offset;
        this.pageSize = builder.pageSize;
    }

    public String getAlbumId() {
        return albumId;
    }

    public List<String> getTags() {
        return tags;
    }

    public int getOffset() {
        return offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public static PhotoSearchRequestBuilder create() {
        return new PhotoSearchRequestBuilder();
    }
}