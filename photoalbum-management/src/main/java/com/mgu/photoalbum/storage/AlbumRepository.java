package com.mgu.photoalbum.storage;

import com.google.inject.Inject;
import com.mgu.photoalbum.adapter.couchdb.DocumentRepository;
import com.mgu.photoalbum.domain.Album;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.ektorp.support.Views;

import java.util.List;

@Views({
        @View(name = "all", map = "function(doc) { if (doc.type == '" + Album.DOCUMENT_TYPE + "') emit(null, doc._id)}"),
        @View(name = "by_owner", map = "function(doc) { if ('type' in doc && doc.type === 'album') { if ('ownerId' in doc) { emit(doc.ownerId, doc); } } }")
})
public class AlbumRepository extends DocumentRepository<Album> {

    @Inject
    public AlbumRepository(final CouchDbConnector connector) {
        super(Album.class, connector);
        initStandardDesignDocument();
    }

    public List<Album> getAllByOwner(final String ownerId) {
        final ViewQuery query = new ViewQuery()
                .designDocId("_design/Album")
                .viewName("by_owner")
                .includeDocs(true)
                .key(ownerId);
        return connector.queryView(query, Album.class);
    }
}