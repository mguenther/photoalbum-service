package com.mgu.photoalbum.service;

import com.mgu.photoalbum.exception.PhotoalbumException;

public class ImageDoesNotExistException extends PhotoalbumException {

    private static final String ERROR_MESSAGE = "The image file for photo with ID %s does not exist on the filesystem.";

    public ImageDoesNotExistException(final String photoId) {
        super(String.format(ERROR_MESSAGE, photoId));
    }

    @Override
    public String getErrorCode() {
        return "P004";
    }
}