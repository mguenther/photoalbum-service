package com.mgu.photoalbum.webapp.representation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class UpdateMetadataRepr {

    private final String description;

    private final List<String> tags;

    @JsonCreator
    public UpdateMetadataRepr(@JsonProperty("description") String description, @JsonProperty("tags") List<String> tags) {
        this.description = description;
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }
}