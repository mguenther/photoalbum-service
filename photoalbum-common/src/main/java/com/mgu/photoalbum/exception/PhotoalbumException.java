package com.mgu.photoalbum.exception;

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