package com.mgu.photoalbum.service.scaler;

import com.mgu.photoalbum.exception.PhotoalbumException;

/**
 * This exception is raised if the image scaling fails, retaining the root cause (which could be I/O and/or
 * an algorithmic error - we don't actually care at this point).
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class UnableToScaleImageException extends PhotoalbumException {

    private static final String ERROR_MESSAGE = "Unable to scale given image data.";

    public UnableToScaleImageException() {
        super(ERROR_MESSAGE);
    }

    public UnableToScaleImageException(final Throwable cause) {
        super(ERROR_MESSAGE, cause);
    }

    @Override
    public String getErrorCode() {
        return "P002";
    }
}