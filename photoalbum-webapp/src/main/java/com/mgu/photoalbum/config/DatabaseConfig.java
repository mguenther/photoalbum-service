package com.mgu.photoalbum.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class DatabaseConfig {

    @NotEmpty
    @JsonProperty("url")
    private String url;

    @NotEmpty
    @JsonProperty("name")
    private String name;

    @JsonProperty("user")
    private String user;

    @JsonProperty("password")
    private String password;

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(final String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}