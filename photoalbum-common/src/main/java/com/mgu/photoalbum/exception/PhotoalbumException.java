package com.mgu.photoalbum.exception;

/**
 * This is the base exception type for all exceptions within the photoalbum service. It provides the
 * means to retain the cause (if there is any) as well as an error message and an error code.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
abstract public class PhotoalbumException extends RuntimeException {

    private static final long serialVersionUID = -8563374709814927511L;

    public PhotoalbumException(final String message) {
        super(message);
    }

    public PhotoalbumException(final Throwable cause) {
        super(cause);
    }

    public PhotoalbumException(final String message, final Throwable cause) {
        super(message, cause);
    }

    abstract public String getErrorCode();
}