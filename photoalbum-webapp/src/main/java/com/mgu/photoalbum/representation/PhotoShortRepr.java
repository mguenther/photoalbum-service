package com.mgu.photoalbum.representation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

public class PhotoShortRepr {

    public static class PhotoShortReprBuilder {

        private String description;
        private List<String> tags = new LinkedList<>();
        private List<LinkRepr> links = new LinkedList<>();

        public PhotoShortReprBuilder description(final String description) {
            this.description = description;
            return this;
        }

        public PhotoShortReprBuilder tags(final List<String> tags) {
            this.tags.addAll(tags);
            return this;
        }

        public PhotoShortReprBuilder link(final LinkRepr link) {
            this.links.add(link);
            return this;
        }

        public PhotoShortRepr build() {
            return new PhotoShortRepr(this);
        }
    }

    @JsonProperty("description")
    private final String description;

    @JsonProperty("tags")
    private final List<String> tags;

    @JsonProperty("links")
    private final List<LinkRepr> links;

    private PhotoShortRepr(final PhotoShortReprBuilder builder) {
        this.description = builder.description;
        this.tags = builder.tags;
        this.links = builder.links;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<LinkRepr> getLinks() {
        return links;
    }

    public static PhotoShortReprBuilder create() {
        return new PhotoShortReprBuilder();
    }
}