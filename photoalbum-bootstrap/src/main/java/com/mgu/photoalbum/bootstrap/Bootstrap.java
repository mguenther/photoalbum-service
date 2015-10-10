package com.mgu.photoalbum.bootstrap;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import java.util.ArrayList;
import java.util.List;

public class Bootstrap implements Generator {

    private final List<Generator> generators = new ArrayList<>();

    private final CouchDbConnector connector;

    public Bootstrap(final String databaseName) {
        connector = establishConnection(databaseName);
        generators.add(new UserGenerator(connector));
        generators.add(new AlbumGenerator(connector));
        generators.add(new PhotoGenerator(connector));
    }

    private CouchDbConnector establishConnection(final String databaseName) {
        try {
            final HttpClient httpClient = new StdHttpClient.Builder()
                    .url("http://localhost:5984")
                    .connectionTimeout(1000)
                    .socketTimeout(1000)
                    .build();
            final CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
            return new StdCouchDbConnector(databaseName, dbInstance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void generate() {
        this.connector.createDatabaseIfNotExists();
        for (Generator generator : this.generators) {
            System.out.println();
            System.out.println("***************************************************************************");
            System.out.println("** Running " + generator.toString());
            System.out.println("***************************************************************************");
            System.out.println();
            generator.generate();
        }
    }

    public static void main(String[] args) {
        try {
            final Bootstrap bootstrap = new Bootstrap("test-photoalbum");
            bootstrap.generate();
        } catch (Throwable t) {
            System.err.println("Caught exception: " + t.getMessage());
            t.printStackTrace(System.err);
        }
    }
}