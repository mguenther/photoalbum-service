package com.mgu.photoalbum.adapter.couchdb;

import org.ektorp.CouchDbConnector;
import org.ektorp.DbInfo;
import org.ektorp.changes.ChangesCommand;
import org.ektorp.changes.ChangesFeed;
import org.ektorp.changes.DocumentChange;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Establishes a continuous update feed to a CouchDB instance. Gets notified via
 * this feed on any updates to documents of the connected database. Updates are
 * database-wide, irregardless of the specific document type. Its responsibility
 * is to keep track of a constant stream of updates by maintaining a local sequence
 * number which represents the sequence number associated with the last update
 * that was processed by this class. This sequence number may be initialized with
 * a specific value upon object construction. <code>CouchDbChangesWorker</code>
 * notifies <code>DocumentChangeListener</code> on the basis of the document type
 * that they are interested in. The current design of this class allows only one
 * <code>DocumentChangeListener</code> for a given document type.
 *
 * <code>CouchDbChangesWorker</code> is supposed to run in its own {@link Thread}.
 * If clients need to stop <code>CouchDbChangesWorker</code> they should call
 * {@link CouchDbChangesWorker#stop()}. <b>This class will not react to
 * {@link Thread#interrupt()}!</b> Doing so anyways will result in the termination
 * of the run-loop and thus the termination of the {@link Thread} which runs
 * <code>CouchDbChangesWorker</code> (and the changes feed).
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class CouchDbChangesWorker implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(CouchDbChangesWorker.class.getName());

    /**
     * Represents the common field used for the specific document type a
     * CouchDB document has.
     */
    private static final String COUCHDB_FIELD_DOCUMENT_TYPE = "type";

    /**
     * Abstraction over the actual CouchDB database this <code>CouchDbChangesWorker</code>
     * is connected to.
     */
    private final CouchDbConnector connector;

    /**
     * Collection of <code>DocumentChangeListener</code> on <code>String</code>-based
     * values, organized by document type. Currently, this class only allows a single
     * <code>DocumentChangeListener</code> per document type.
     */
    private final Map<String, DocumentChangeListener> listenerByType;

    /**
     * Abstraction over the CouchDB changes feed.
     */
    private ChangesFeed changesFeed;

    /**
     * Represents the last sequence number that this <code>CouchDbChangesWorker</code>
     * has been notified about. Upon object construction, this value will get pre-initialized
     * in order to avoid fetching all updates that have been made to the database since its
     * inception. The pre-initialization may be done manually, using
     * {@link CouchDbChangesWorker(long, CouchDbConnector, Map)} or automatically using
     * {@link CouchDbChangesWorker(CouchDbConnector, Map)}. In the latter case, the current
     * database connection (cf. <code>CouchDbConnector</code>) is used to obtain the
     * sequence number of the last commit to the database.
     */
    private long lastSequenceNumber;

    /**
     * Indicates whether this <code>CouchDbChangesWorker</code> is running. Clients
     * of this class should use {@link CouchDbChangesWorker#stop()} to terminate
     * its execution.
     */
    private volatile boolean running = true;

    /**
     * Constructs a <code>CouchDbChangesWorker</code> without providing an explicit
     * last sequence number. The last sequence number is obtained by querying the
     * database.
     *
     * @param connector
     *      Abstraction over the actual CouchDB database this <code>CouchDbChangesWorker</code>
     *      is connected to. Must not be <code>null</code>.
     * @param listenerByType
     *      Collection of <code>DocumentChangeListener</code> on <code>String</code>-based
     *      values, organized by document type.
     * @throws IllegalArgumentException
     *      in case parameter <code>connector</code> is <code>null</code>
     */
    public CouchDbChangesWorker(
            final CouchDbConnector connector,
            final Map<String, DocumentChangeListener> listenerByType) {

        if (connector == null) {
            throw new IllegalArgumentException("CouchDbConnector cannot be null.");
        }

        if (listenerByType == null) {
            throw new IllegalArgumentException("Map of DocumentChangeListener cannot be null.");
        }

        this.lastSequenceNumber = getSequenceNumber(connector);
        this.connector = connector;
        this.listenerByType = listenerByType;
    }

    /**
     * Constructs a <code>CouchDbChangesWorker</code> using a manually
     * set sequence number to start off with.
     *
     * @param connector
     *      Abstraction over the actual CouchDB database this <code>CouchDbChangesWorker</code>
     *      is connected to. Must not be <code>null</code>.
     * @param listenerByType
     *      Collection of <code>DocumentChangeListener</code> on <code>String</code>-based
     *      values, organized by document type.
     * @param currentSequenceNumber
     *      Sequence number for initializing the value of the last seen sequence number
     *      of this <code>CouchDbChangesWorker</code>.
     * @throws IllegalArgumentException
     *      in case parameter <code>connector</code> is <code>null</code>
     */
    public CouchDbChangesWorker(
            final CouchDbConnector connector,
            final Map<String, DocumentChangeListener> listenerByType,
            final int currentSequenceNumber) {

        if (connector == null) {
            throw new IllegalArgumentException("CouchDbConnector cannot be null.");
        }

        if (listenerByType == null) {
            throw new IllegalArgumentException("Map of DocumentChangeListeners cannot be null.");
        }

        this.lastSequenceNumber = currentSequenceNumber;
        this.connector = connector;
        this.listenerByType = listenerByType;
    }

    /**
     * Yields the sequence number of the last commit to the connected database.
     *
     * @param connector
     *      Abstraction over the connection to the CouchDB database
     * @return
     *      the sequence number of the last commit to the connected
     *      CouchDB database
     */
    private long getSequenceNumber(final CouchDbConnector connector) {
        final DbInfo dbInfo = connector.getDbInfo();
        return dbInfo.getUpdateSeq();
    }

    @Override
    public void run() {
        establishFeed();

        while (isRunning()) {

            if (!this.changesFeed.isAlive()) {
                LOGGER.info("CouchDB changes feed has died, re-establishing connection now.");
                establishFeed();
            }

            try {
                final DocumentChange documentChange = this.changesFeed.next();

                if ((documentChange != null) && (documentChange.getDoc() != null)) {
                    onDocumentChanged(documentChange);
                }
            } catch (InterruptedException e) {
                if (isRunning()) {
                    LOGGER.info("Received InterruptedException from ChangesFeed, " +
                                "effectively stopping it. Will try to re-establish " +
                                "it.");
                } else {
                    LOGGER.info("Received an InterruptedException from ChangesFeed, " +
                                "effectively stoppig it. Taking no countermeasures " +
                                "since this worker has been stopped too.");
                }
            }
        }
    }

    /**
     * Establishes the HTTP-based update feed to the connected database.
     */
    private void establishFeed() {
        final ChangesCommand changesCommand = new ChangesCommand.Builder()
                .since(this.lastSequenceNumber)
                .includeDocs(true)
                .continuous(true)
                .build();
        this.changesFeed = this.connector.changesFeed(changesCommand);
    }

    /**
     * Handles an atomic update by adjusting the local sequence number to the new
     * value (sequence numbers are ever-increasing) and triggers notifications.
     *
     * @param documentChange
     *      Abstraction of an updated document, including metadata like associated
     *      sequence number, the updated document itself, and the like.
     */
    private void onDocumentChanged(final DocumentChange documentChange) {
        updateSequenceNumber(documentChange);
        notifyListener(documentChange);
    }

    /**
     * Extracts the sequence number associated with <code>DocumentChange</code>
     * and sets it as the last seen and processed sequence number by this
     * <code>CouchDbChangesWorker</code>.
     *
     * @param documentChange
     *      Abstraction of an updated document, including metadata like associated
     *      sequence number, the updated document itself, and the like.
     */
    private void updateSequenceNumber(final DocumentChange documentChange) {
        LOGGER.fine("Increased sequence number from " + this.lastSequenceNumber +
                " to " + documentChange.getSequence());
        this.lastSequenceNumber = documentChange.getSequence();
    }

    /**
     * Looks up the {@link DocumentChangeListener} that listens for changes
     * on the document type of the updated document. If there is no such
     * {@link DocumentChangeListener} or the updated document does not provide
     * a type field, the notification will fail silently (no update of local
     * state) but will provide a detailed log message as to why the
     * notification has failed.
     *
     * @param documentChange
     *      Abstraction of an updated document, including metadata like associated
     *      sequence number, the updated document itself, and the like.
     */
    private void notifyListener(final DocumentChange documentChange) {
        if (!hasTypeField(documentChange)) {
            LOGGER.warning("Tried to handle a document update from CouchDB but was unable " +
                           "to find a field '" + COUCHDB_FIELD_DOCUMENT_TYPE + "' containing " +
                           "the actual document type. Skipping this document update.");
            return;
        }

        final String documentType = documentChange
                .getDocAsNode()
                .get(COUCHDB_FIELD_DOCUMENT_TYPE)
                .asText();

        if (!this.listenerByType.containsKey(documentType)) {
            LOGGER.warning("Tried to handle a document update from CouchDB but there is no " +
                           "DocumentChangeListener registered for document type " +
                           "'" + documentType + "'. Skipping this document update.");
            return;
        }

        final DocumentChangeListener changeListener = this.listenerByType.get(documentType);
        if (documentChange.isDeleted()) {
            changeListener.onDocumentDeleted(documentChange.getId(), documentChange.getSequence());

            LOGGER.info("Propagated document deletion for document with ID '" + documentChange.getId() +
                        " to listener '" + changeListener.getClass() + "'.");
        } else {
            final String serializedDocument = documentChange.getDoc();
            changeListener.onDocumentModified(serializedDocument, documentType, documentChange.getSequence());

            LOGGER.info("Propagated document modification for document with ID '" + documentChange.getId() +
                        " to listener '" + changeListener.getClass() + "'.");
        }
    }

    /**
     * Checks if the update document wrapped by {@link DocumentChange} has a type field.
     *
     * @param documentChange
     *      Abstraction of an updated document, including metadata like associated
     *      sequence number, the updated document itself, and the like.
     * @return
     *      <code>true</code> if the document has a type field, <code>false</code>
     *      otherwise
     */
    private boolean hasTypeField(final DocumentChange documentChange) {
        return documentChange.getDocAsNode().get(COUCHDB_FIELD_DOCUMENT_TYPE) != null;
    }

    /**
     * @return
     *      Determines whether this <code>CouchDbChangesWorker</code> is still running
     */
    public boolean isRunning() {
        return this.running;
    }

    /**
     * Terminates the execution of this <code>CouchDbChangesWorker</code>.
     */
    public void stop() {
        this.running = false;
        this.changesFeed.cancel();
    }
}