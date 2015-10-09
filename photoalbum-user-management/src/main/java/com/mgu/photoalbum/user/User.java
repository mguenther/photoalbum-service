package com.mgu.photoalbum.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mgu.photoalbum.adapter.couchdb.Document;

public class User extends Document {

    public static final String DOCUMENT_TYPE = "user";

    private static final long serialVersionUID = -54352348232L;

    private String firstName;

    private String lastName;

    private String email;

    private String hashedPassword;

    private Status status;

    public User() {
        super(DOCUMENT_TYPE);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(final String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    @JsonIgnore
    public boolean isSuspended() {
        return this.status == Status.SUSPENDED;
    }

    @JsonIgnore
    public boolean isActive() {
        return this.status == Status.ACTIVE;
    }

    @JsonIgnore
    public boolean isPending() {
        return this.status == Status.PENDING;
    }
}