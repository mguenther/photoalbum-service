package com.mgu.photoalbum.adapter.couchdb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * This class provides technical metadata for root documents. A root document represents
 * the root object of an object graph of an document structure. Every root document within
 * the technical data model <emph>must</emph> derive from this class.
 *
 * Documents are comprised of a Database-wide unique ID, a revision number, and a type
 * associated with them. While a client may set the ID initially as well as the type of
 * the document, a client may never set the revision of a document. Although there is
 * a method {@link Document#setRevision(String)} to satisfy the Java bean convention,
 * clients are not supposed to access and modify it.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
abstract public class Document implements Serializable {

    private static final long serialVersionUID = 23748920342L;

    /**
     * The unique ID associated with this instance of {@link Document}. This ID may
     * not be <code>null</code> and is immutable if set once.
     */
    private String id;

    /**
     * The revision associated with this instance of {@link Document}.
     */
    private String revision;

    /**
     * Every {@link Document} must adhere to a certain type. The type may not be
     * <code>null</code> and is immutable if set once.
     */
    @JsonProperty("type")
    private String type;

    /**
     * Consumes type in order to force derived documents to pass in their
     * document type.
     *
     * @param type
     *      type of this document
     */
    public Document(final String type) {
        this.type = type;
    }

    /**
     * Sets the ID of this {@link Document}. IDs are immutable and cannot be modified
     * if set once.
     *
     * @param id
     *      <code>String</code> representing the ID of this <code>Document</code>
     * @throws IllegalArgumentException
     *      if the given ID is <code>null</code> or an empty <code>String</code>
     * @throws IllegalStateException
     *      if the ID has been set before
     */
    @JsonProperty("_id")
    public void setId(final String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("ID must not be null.");
        }
        if (this.id != null) {
            throw new IllegalStateException("ID has already been set to '" + this.id + "'.");
        }
        this.id = id;
    }

    /**
     * @return
     *      the ID associated with this {@link Document}
     */
    @JsonProperty("_id")
    public String getId() {
        return this.id;
    }

    /**
     * Sets the revision of this {@link Document}. Clients are not supposed to call
     * this method. It only exists to comply with the Java beans convention.
     *
     * @param revision
     *      the new revision associated with this <code>Document</code>
     */
    @JsonProperty("_rev")
    public void setRevision(final String revision) {
        this.revision = revision;
    }

    /**
     * @return
     *      the revision associated with this {@link Document}
     */
    @JsonProperty("_rev")
    public String getRevision() {
        return this.revision;
    }

    /**
     * Sets the type of this {@link Document}.
     *
     * @param type
     *      represents the document type
     * @throws IllegalArgumentException
     *      if the given type is <code>null</code> or the empty <code>String</code>
     * @throws IllegalStateException
     *      if the type has been set before
     */
    @JsonIgnore
    public void setType(final String type) {
        if (StringUtils.isEmpty(type)) {
            throw new IllegalArgumentException("Given type must not be null.");
        }
        if (this.type != null) {
            throw new IllegalStateException("Type has already been set to '" + this.type + "'.");
        }
        this.type = type;
    }

    /**
     * @return
     *      the type associated with this {@link Document}
     */
    public final String getType() {
        return this.type;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Document document = (Document) o;

        return !(id != null ? !id.equals(document.id) : document.id != null);

    }

    @Override
    public final int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}