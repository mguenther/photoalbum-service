package com.mgu.photoalbum.representation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MetaRepr {

    public static class MetaReprBuilder {

        private Map<String, LinkRepr> namedLinks = new HashMap<>();

        public MetaReprBuilder link(final LinkRepr link) {
            this.namedLinks.put(link.getRelation(), link);
            return this;
        }

        public MetaRepr build() {
            return new MetaRepr(this);
        }
    }

    @JsonProperty("links")
    private final Map<String, LinkRepr> namedLinks;

    private MetaRepr(final MetaReprBuilder builder) {
        this.namedLinks = builder.namedLinks;
    }

    public Map<String, LinkRepr> getNamedLinks() {
        return Collections.unmodifiableMap(this.namedLinks);
    }

    public static MetaReprBuilder create() {
        return new MetaReprBuilder();
    }
}