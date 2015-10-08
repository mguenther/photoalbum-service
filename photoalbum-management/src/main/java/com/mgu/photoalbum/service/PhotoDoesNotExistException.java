package com.mgu.photoalbum.service;

import com.mgu.photoalbum.exception.PhotoalbumException;

public class PhotoDoesNotExistException extends PhotoalbumException {

    private static final String ERROR_MESSAGE = "The photo with ID %s does not exist.";

    public PhotoDoesNotExistException(final String photoId) {
        super(String.format(ERROR_MESSAGE, photoId));
    }

    @Override
    public String getErrorCode() {
        return "P001";
    }
}