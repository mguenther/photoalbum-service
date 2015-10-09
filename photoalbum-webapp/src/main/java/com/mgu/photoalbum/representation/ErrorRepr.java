package com.mgu.photoalbum.representation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ErrorRepr {

    @JsonProperty("errorCode")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String errorCode;

    @JsonProperty("httpStatus")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private int httpStatus;

    @JsonProperty("title")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String title;

    @JsonProperty("details")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private List<String> details = new ArrayList<>();

    @JsonProperty("describedBy")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LinkRepr describedBy;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(final int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(final List<String> details) {
        this.details = details;
    }

    public LinkRepr getDescribedBy() {
        return describedBy;
    }

    public void setDescribedBy(final LinkRepr describedBy) {
        this.describedBy = describedBy;
    }
}