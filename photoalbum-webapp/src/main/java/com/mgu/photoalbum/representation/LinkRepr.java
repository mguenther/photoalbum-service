package com.mgu.photoalbum.representation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.util.List;

public class LinkRepr {

    @JsonIgnore
    private String relation;

    @JsonProperty("href")
    private String href;

    @JsonProperty("methods")
    private List<String> admissibleMethods;

    public String getRelation() {
        return relation;
    }

    public void setRelation(final String relation) {
        this.relation = relation;
    }

    public String getHref() {
        return href;
    }

    public void setHref(final URI href) {
        this.href = href.toString();
    }

    public List<String> getAdmissibleMethods() {
        return admissibleMethods;
    }

    public void setAdmissibleMethods(final List<String> admissibleMethods) {
        this.admissibleMethods = admissibleMethods;
    }
}