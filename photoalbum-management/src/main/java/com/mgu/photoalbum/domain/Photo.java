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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Photo extends Document {

    public static final String DOCUMENT_TYPE = "photo";

    public static class PhotoBuilder {

        private String photoId = StringUtils.EMPTY;

        private String ownerId = StringUtils.EMPTY;

        private String albumId = StringUtils.EMPTY;

        private DateTime created = DateTime.now(DateTimeZone.UTC);

        private DateTime lastModified = DateTime.now(DateTimeZone.UTC);

        private String originalFilename = StringUtils.EMPTY;

        private String pathToOriginal = StringUtils.EMPTY;

        private String pathToThumbnail = StringUtils.EMPTY;

        private List<String> tags = new ArrayList<>();

        public PhotoBuilder id(final String photoId) {
            this.photoId = photoId;
            return this;
        }

        public PhotoBuilder belongsTo(final String albumId) {
            this.albumId = albumId;
            return this;
        }

        public PhotoBuilder createdBy(final String ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public PhotoBuilder originalFilename(final String originalFilename) {
            this.originalFilename = originalFilename;
            return this;
        }

        public PhotoBuilder pathToOriginal(final String pathToOriginal) {
            this.pathToOriginal = pathToOriginal;
            return this;
        }

        public PhotoBuilder pathToThumbnail(final String pathToThumbnail) {
            this.pathToThumbnail = pathToThumbnail;
            return this;
        }

        public PhotoBuilder tag(final String tag) {
            this.tags.add(tag);
            return this;
        }

        public PhotoBuilder tag(final List<String> tags) {
            this.tags.addAll(tags);
            return this;
        }

        public Photo build() {
            return new Photo(this);
        }
    }

    @JsonProperty("ownerId")
    private String ownerId;

    @JsonProperty("albumId")
    private String albumId;

    @JsonProperty("created")
    @JsonSerialize(using = UtcIso8601Serializer.class)
    @JsonDeserialize(using = UtcIso8601Deserializer.class)
    private DateTime created;

    @JsonProperty("lastModified")
    @JsonSerialize(using = UtcIso8601Serializer.class)
    @JsonDeserialize(using = UtcIso8601Deserializer.class)
    private DateTime lastModified;

    @JsonProperty("originalFilename")
    private String originalFilename;

    @JsonProperty("pathToOriginal")
    private String pathToOriginal;

    @JsonProperty("pathToThumbnail")
    private String pathToThumbnail;

    @JsonProperty("tags")
    private List<String> tags = new ArrayList<>();

    public Photo() {
        super(DOCUMENT_TYPE);
    }

    private Photo(final PhotoBuilder builder) {
        super(DOCUMENT_TYPE);
        setId(builder.photoId);
        this.albumId = builder.albumId;
        this.ownerId = builder.ownerId;
        this.created = builder.created;
        this.lastModified = builder.lastModified;
        this.originalFilename = builder.originalFilename;
        this.pathToOriginal = builder.pathToOriginal;
        this.pathToThumbnail = builder.pathToThumbnail;
        this.tags.addAll(builder.tags);
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public DateTime getCreated() {
        return created;
    }

    public DateTime getLastModified() {
        return lastModified;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getPathToOriginal() {
        return pathToOriginal;
    }

    public String getPathToThumbnail() {
        return pathToThumbnail;
    }

    public List<String> getTags() {
        return tags;
    }

    public void tag(final String tag) {
        if (this.tags.contains(tag)) {
            return;
        }
        this.tags.add(tag);
        this.lastModified = DateTime.now(DateTimeZone.UTC);
    }

    public void tag(final List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return;
        }
        this.tags.addAll(tags);
        this.tags = this.tags.stream().distinct().collect(Collectors.toList());
        this.lastModified = DateTime.now(DateTimeZone.UTC);
    }

    public void untag() {
        this.tags.clear();
        this.lastModified = DateTime.now(DateTimeZone.UTC);
    }

    public static PhotoBuilder create() {
        return new PhotoBuilder();
    }
}