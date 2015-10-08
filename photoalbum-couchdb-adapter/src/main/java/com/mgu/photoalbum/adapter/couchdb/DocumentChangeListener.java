package com.mgu.photoalbum.adapter.couchdb;

/**
 * Listener that gets notified if modifications to documents have occured.
 * Modified documents are represented in their raw JSON form, thus untyped.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public interface DocumentChangeListener {

    /**
     * Gets called if the data represented by an instance of parameterized
     * type <code>T</code> has been modified.
     *
     * @param modifiedDocument
     *      Represents the modified document in JSON representation
     * @param documentType
     *      Represents the type of the document (documents that have no type
     *      are not eligible for document changes)
     * @param sequenceNumber
     *      Represents the associated sequence number of the modification
     */
    void onDocumentModified(String modifiedDocument, String documentType, int sequenceNumber);

    /**
     * Gets called if the data associated with the given ID has been
     * deleted.
     *
     * @param documentId
     *      The document ID of the deleted document
     * @param sequenceNumber
     *      Represents the associated sequence number of the deletion
     */
    void onDocumentDeleted(String documentId, int sequenceNumber);
}
