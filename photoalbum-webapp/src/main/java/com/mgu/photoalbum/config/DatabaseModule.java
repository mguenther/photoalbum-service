package com.mgu.photoalbum.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mgu.photoalbum.config.DatabaseConfig;
import com.mgu.photoalbum.config.ServiceConfig;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

public class DatabaseModule extends AbstractModule {

    @Provides
    public CouchDbConnector provideConnection(final ServiceConfig config) {
        final DatabaseConfig database = config.getDatabaseConfig();
        try {
            final HttpClient httpClient = new StdHttpClient.Builder()
                    .url(database.getUrl())
                    .connectionTimeout(1000)
                    .socketTimeout(1000)
                    .build();
            final CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
            return new StdCouchDbConnector(database.getName(), dbInstance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void configure() {
        // TODO: Wire repositories!
    }
}