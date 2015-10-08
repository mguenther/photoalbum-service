package com.mgu.photoalbum.adapter.couchdb;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.ektorp.CouchDbConnector;
import org.ektorp.DbAccessException;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * Abstract base class for document repositories which provides exception handling.
 *
 * @param <T>
 *        generic type of the Document repository, to be specified by
 *        implementing classes; restricted to derivatives of
 *        {@link Document}
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
abstract public class DocumentRepository<T extends Document> extends CouchDbRepositorySupport<T> implements DocumentChangeListener {

    private static final Logger LOGGER = Logger.getLogger(DocumentRepository.class.getName());

    /**
     * The connector to the CouchDB database in which documents of
     * parameterized type <code>T</code> will be stored.
     */
    protected CouchDbConnector connector;

    /**
     * The constructor consumes {@link Class} that corresponds to the parameterized
     * type <code>T</code> as well as an instance of {@link CouchDbConnector}. This is
     * required to ensure compatibility with the super class.
     *
     * @param clazz
     *      {@link Class} that corresponds to the parameterized type <code>T</code>
     * @param connector
     *      Abstraction over the connection to the CouchDB database
     */
    public DocumentRepository(final Class<T> clazz, final CouchDbConnector connector) {
        super(clazz, connector);
        this.connector = connector;
    }

    /**
     * Handles a catched {@link ExecutionException} and unravels it to dispatch
     * the cause to the respective handler method.
     *
     * @param e
     *      {@link ExecutionException} which was thrown when the repository tries
     *      to access the database and the operation fails. The real cause is
     *      wrapped inside this exception
     */
    protected final void handleException(final ExecutionException e) {
        final Throwable t = e.getCause();
        if (t instanceof DocumentNotFoundException) {
            handleException((DocumentNotFoundException) t);
        } else if (t instanceof DbAccessException) {
            final DbAccessException t2 = (DbAccessException) t;
            if (t2.getCause() instanceof UnrecognizedPropertyException) {
                handleException((UnrecognizedPropertyException) t2.getCause());
            } else {
                handleException(t2);
            }
        }
    }

    /**
     * Handles a catched {@link DocumentNotFoundException}.
     *
     * @param e
     *      {@link DocumentNotFoundException} which is thrown when the repository
     *      tries to access a document with a non-existing document ID
     */
    protected void handleException(final DocumentNotFoundException e) {
        LOGGER.warning("Requested document does not exist.");
    }

    /**
     * Handles a catched {@link DbAccessException}.
     *
     * @param e
     *      {@link DbAccessException} which is thrown when the repository tries
     *      to access the database, but the database host is unreachable
     *      (possibly due to host-failure or database-failure)
     */
    protected void handleException(final DbAccessException e) {
        LOGGER.warning("Unable to access the database.");
    }

    /**
     * Handles a catched {@link UnrecognizedPropertyException}.
     *
     * @param e
     *      {@link UnrecognizedPropertyException} which is thrown when the
     *      repository tries to access a document with a certain ID, but the
     *      stored document does not match the Java type we expect it to
     *      unmarshal to
     */
    protected void handleException(final UnrecognizedPropertyException e) {
        LOGGER.warning("Unable to unmarshal JSON document into document instance.");
    }

    @Override
    public void onDocumentModified(final String modifiedDocument, final String documentType, final int sequenceNumber) {
        // NO-OP
    }

    @Override
    public void onDocumentDeleted(final String documentId, final int sequenceNumber) {
        // NO-OP
    }
}