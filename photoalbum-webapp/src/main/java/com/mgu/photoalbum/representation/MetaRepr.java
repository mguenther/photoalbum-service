package com.mgu.photoalbum.representation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

public class MetaRepr {

    @JsonProperty("links")
    private Map<String, LinkRepr> namedLinks = new LinkedHashMap<>();

    public Map<String, LinkRepr> getNamedLinks() {
        return namedLinks;
    }

    public void setNamedLinks(final Map<String, LinkRepr> namedLinks) {
        this.namedLinks = namedLinks;
    }

    public void addNamedLink(final LinkRepr namedLink) {
        this.namedLinks.put(namedLink.getRelation(), namedLink);
    }
}