package com.mgu.photoalbum.storage;

import com.google.inject.Inject;
import com.mgu.photoalbum.adapter.couchdb.DocumentRepository;
import com.mgu.photoalbum.domain.Photo;
import org.ektorp.CouchDbConnector;

public class PhotoRepository extends DocumentRepository<Photo> {

    @Inject
    public PhotoRepository(final CouchDbConnector connector) {
        super(Photo.class, connector);
        initStandardDesignDocument();
    }
}