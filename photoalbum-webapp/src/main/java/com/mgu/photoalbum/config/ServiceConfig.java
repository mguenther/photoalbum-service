package com.mgu.photoalbum.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class ServiceConfig extends Configuration {

    @JsonProperty("database")
    private DatabaseConfig databaseConfig;

    @JsonProperty("photoalbum")
    private PhotoalbumConfig photoalbumConfig;

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    public void setDatabaseConfig(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public PhotoalbumConfig getPhotoalbumConfig() {
        return photoalbumConfig;
    }

    public void setPhotoalbumConfig(PhotoalbumConfig photoalbumConfig) {
        this.photoalbumConfig = photoalbumConfig;
    }
}