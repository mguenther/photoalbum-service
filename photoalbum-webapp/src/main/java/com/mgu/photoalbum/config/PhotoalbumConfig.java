package com.mgu.photoalbum.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

public class PhotoalbumConfig {

    @JsonProperty("imageArchivePath")
    @NotEmpty
    private String imageArchivePath;

    @JsonProperty("maxCacheAge")
    @Range(min = 0)
    private int maxCacheAge;

    @JsonProperty("hostname")
    @NotEmpty
    private String hostname;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    public String getImageArchivePath() {
        return imageArchivePath;
    }

    public void setImageArchivePath(final String imageArchivePath) {
        this.imageArchivePath = imageArchivePath;
    }

    public int getMaxCacheAge() {
        return maxCacheAge;
    }

    public void setMaxCacheAge(final int maxCacheAge) {
        this.maxCacheAge = maxCacheAge;
    }
}
