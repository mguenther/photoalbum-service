package com.mgu.photoalbum.storage;

import com.google.inject.Inject;
import com.mgu.photoalbum.adapter.couchdb.DocumentRepository;
import com.mgu.photoalbum.domain.Album;
import org.ektorp.CouchDbConnector;

public class AlbumRepository extends DocumentRepository<Album> {

    @Inject
    public AlbumRepository(final CouchDbConnector connector) {
        super(Album.class, connector);
        initStandardDesignDocument();
    }
}