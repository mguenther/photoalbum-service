package com.mgu.photoalbum.security;

import com.mgu.photoalbum.exception.PhotoalbumException;

public class UserIsNotAuthorizedException extends PhotoalbumException {

    private static final String ERROR_MESSAGE = "%s (ID: %s) is not authorized to perform the requested action.";

    public UserIsNotAuthorizedException(final Principal principal) {
        super(String.format(ERROR_MESSAGE, principal.getDisplayName(), principal.getUserId()));
    }

    @Override
    public String getErrorCode() {
        return "G005";
    }
}