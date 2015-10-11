package com.mgu.photoalbum.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mgu.photoalbum.adapter.couchdb.Document;
import com.mgu.photoalbum.adapter.couchdb.UtcIso8601Deserializer;
import com.mgu.photoalbum.adapter.couchdb.UtcIso8601Serializer;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class Album extends Document {

    public static final String DOCUMENT_TYPE = "album";

    private static final long serialVersionUID = -248239042830942L;

    public static class AlbumBuilder {

        private String albumId = StringUtils.EMPTY;

        private String ownerId = StringUtils.EMPTY;

        private String title = StringUtils.EMPTY;

        private DateTime created = DateTime.now(DateTimeZone.UTC);

        private DateTime lastModified = DateTime.now(DateTimeZone.UTC);

        public AlbumBuilder id(final String albumId) {
            this.albumId = albumId;
            return this;
        }

        public AlbumBuilder createdBy(final String ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public AlbumBuilder title(final String title) {
            this.title = title;
            return this;
        }

        public Album build() {
            return new Album(this);
        }
    }

    @JsonProperty("ownerId")
    private String ownerId;

    @JsonProperty("created")
    @JsonSerialize(using = UtcIso8601Serializer.class)
    @JsonDeserialize(using = UtcIso8601Deserializer.class)
    private DateTime created;

    @JsonProperty("lastModified")
    @JsonSerialize(using = UtcIso8601Serializer.class)
    @JsonDeserialize(using = UtcIso8601Deserializer.class)
    private DateTime lastModified;

    @JsonProperty("title")
    private String title;

    private Album() {
        super(DOCUMENT_TYPE);
    }

    private Album(final AlbumBuilder builder) {
        super(DOCUMENT_TYPE);
        setId(builder.albumId);
        this.ownerId = builder.ownerId;
        this.created = builder.created;
        this.lastModified = builder.lastModified;
        this.title = builder.title;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public DateTime getCreated() {
        return created;
    }

    public DateTime getLastModified() {
        return lastModified;
    }

    public String getTitle() {
        return title;
    }

    public static AlbumBuilder create() {
        return new AlbumBuilder();
    }
}