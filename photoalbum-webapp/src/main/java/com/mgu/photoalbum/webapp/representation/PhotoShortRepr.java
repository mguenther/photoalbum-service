package com.mgu.photoalbum.webapp.representation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PhotoShortRepr {

    public static class PhotoShortReprBuilder {

        private String photoId;
        private String description;
        private List<String> tags = new LinkedList<>();
        private Map<String, LinkRepr> namedLinks = new HashMap<>();

        public PhotoShortReprBuilder id(final String photoId) {
            this.photoId = photoId;
            return this;
        }

        public PhotoShortReprBuilder description(final String description) {
            this.description = description;
            return this;
        }

        public PhotoShortReprBuilder tags(final List<String> tags) {
            this.tags.addAll(tags);
            return this;
        }

        public PhotoShortReprBuilder link(final LinkRepr link) {
            this.namedLinks.put(link.getRelation(), link);
            return this;
        }

        public PhotoShortRepr build() {
            return new PhotoShortRepr(this);
        }
    }

    @JsonProperty("photoId")
    private final String photoId;

    @JsonProperty("description")
    private final String description;

    @JsonProperty("tags")
    private final List<String> tags;

    @JsonProperty("links")
    private final Map<String, LinkRepr> namedLinks;

    private PhotoShortRepr(final PhotoShortReprBuilder builder) {
        this.photoId = builder.photoId;
        this.description = builder.description;
        this.tags = builder.tags;
        this.namedLinks = builder.namedLinks;
    }

    public String getPhotoId() {
        return this.photoId;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public Map<String, LinkRepr> getNamedLinks() {
        return Collections.unmodifiableMap(namedLinks);
    }

    public static PhotoShortReprBuilder create() {
        return new PhotoShortReprBuilder();
    }
}