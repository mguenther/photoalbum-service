package com.mgu.photoalbum.security;

import com.mgu.photoalbum.user.User;

/**
 * This POJO represents an authenticated user within the photoalbum service. It is barely more
 * than a simple data holder for a user ID.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class Principal {

    private final String userId;

    private final String displayName;

    public Principal(final String userId, final String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Principal from(final User user) {
        final String displayName = user.getFirstName() + " " + user.getLastName();
        return new Principal(user.getId(), displayName);
    }
}