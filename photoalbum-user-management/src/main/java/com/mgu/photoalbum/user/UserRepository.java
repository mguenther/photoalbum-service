package com.mgu.photoalbum.user;

import com.google.inject.Inject;
import com.mgu.photoalbum.adapter.couchdb.DocumentRepository;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.View;

@View(name = "all", map = "function(doc) { if (doc.type == '" + User.DOCUMENT_TYPE + "') emit(null, doc._id)}")
public class UserRepository extends DocumentRepository<User> {

    @Inject
    public UserRepository(final CouchDbConnector connector) {
        super(User.class, connector);
        initStandardDesignDocument();
    }
}