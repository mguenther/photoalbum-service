package com.mgu.photoalbum.service;

import com.mgu.photoalbum.exception.PhotoalbumException;

/**
 * A concurrent update of the associated album prevented the user-requested update
 * of the albums metadata.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class UnableToUpdateAlbumException extends PhotoalbumException {

    private static final String ERROR_MESSAGE = "Unable to update the album with ID %s.";

    public UnableToUpdateAlbumException(final String albumId, final Throwable t) {
        super(String.format(ERROR_MESSAGE, albumId));
    }

    @Override
    public String getErrorCode() {
        return "A002";
    }
}