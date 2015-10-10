package com.mgu.photoalbum.webapp.representation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import java.net.URI;

public class LinkRepr {

    public static class LinkReprBuilder {

        private String relation = StringUtils.EMPTY;
        private String href = StringUtils.EMPTY;
        private String method = HttpMethod.GET;
        private String mediaType = MediaType.APPLICATION_JSON;

        public LinkReprBuilder relation(final String relation) {
            this.relation = relation;
            return this;
        }

        public LinkReprBuilder href(final String href) {
            this.href = href;
            return this;
        }

        public LinkReprBuilder href(final URI href) {
            this.href = href.toString();
            return this;
        }

        public LinkReprBuilder method(final String method) {
            this.method = method;
            return this;
        }

        public LinkReprBuilder mediaType(final String mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public LinkRepr build() {
            return new LinkRepr(this);
        }
    }

    @JsonIgnore
    private final String relation;

    @JsonProperty("href")
    private final String href;

    @JsonProperty("method")
    private final String method;

    @JsonProperty("media")
    private final String mediaType;

    public LinkRepr(final String relation, final String href, final String method, final String mediaType) {
        this.relation = relation;
        this.href = href;
        this.method = method;
        this.mediaType = mediaType;
    }

    private LinkRepr(final LinkReprBuilder builder) {
        this.relation = builder.relation;
        this.href = builder.href;
        this.method = builder.method;
        this.mediaType = builder.mediaType;
    }

    public String getRelation() {
        return relation;
    }

    public String getHref() {
        return href;
    }

    public String getMethod() {
        return method;
    }

    public String getMediaType() {
        return this.mediaType;
    }

    public static LinkReprBuilder create() {
        return new LinkReprBuilder();
    }
}