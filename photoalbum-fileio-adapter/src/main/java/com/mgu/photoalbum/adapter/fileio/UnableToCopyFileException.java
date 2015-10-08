package com.mgu.photoalbum.adapter.fileio;

import com.mgu.photoalbum.exception.PhotoalbumException;

public class UnableToCopyFileException extends PhotoalbumException {

    private static final String ERROR_MESSAGE_ONLY_DESTINATION = "Unable to write data to %s.";

    private static final String ERROR_MESSAGE = "Unable to copy file from %s to %s.";

    public UnableToCopyFileException(final String destination, final Throwable cause) {
        super(String.format(ERROR_MESSAGE_ONLY_DESTINATION, destination), cause);
    }

    public UnableToCopyFileException(final String source, final String destination, final Throwable cause) {
        super(String.format(ERROR_MESSAGE, source, destination), cause);
    }

    @Override
    public String getErrorCode() {
        return "G003";
    }
}