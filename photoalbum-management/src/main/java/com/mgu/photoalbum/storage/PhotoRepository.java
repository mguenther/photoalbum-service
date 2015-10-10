package com.mgu.photoalbum.storage;

import com.google.inject.Inject;
import com.mgu.photoalbum.adapter.couchdb.DocumentRepository;
import com.mgu.photoalbum.domain.Photo;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.ektorp.support.Views;

import java.util.List;

@Views({
        @View(name = "all", map = "function(doc) { if (doc.type == '" + Photo.DOCUMENT_TYPE + "') emit(null, doc._id)}"),
        @View(name = "by_album", map = "function(doc) { if ('type' in doc && doc.type === 'photo') { if ('albumId' in doc) { emit(doc.albumId, doc); } } }")
})
public class PhotoRepository extends DocumentRepository<Photo> {

    @Inject
    public PhotoRepository(final CouchDbConnector connector) {
        super(Photo.class, connector);
        initStandardDesignDocument();
    }

    public List<Photo> getAllByAlbum(final String albumId) {
        final ViewQuery query = new ViewQuery()
                .designDocId("_design/Photo")
                .viewName("by_album")
                .includeDocs(true)
                .key(albumId);
        return connector.queryView(query, Photo.class);
    }
}