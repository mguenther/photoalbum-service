package com.mgu.photoalbum.service;

import org.apache.commons.lang3.StringUtils;

/**
 * Container for Album-related search parameters.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class AlbumSearchRequest {

    public static class AlbumSearchRequestBuilder {

        private String ownerId = StringUtils.EMPTY;

        public AlbumSearchRequestBuilder createdBy(final String ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public AlbumSearchRequest build() {
            return new AlbumSearchRequest(this);
        }
    }

    private final String ownerId;

    private AlbumSearchRequest(final AlbumSearchRequestBuilder builder) {
        this.ownerId = builder.ownerId;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public static AlbumSearchRequestBuilder create() {
        return new AlbumSearchRequestBuilder();
    }
}