package com.mgu.photoalbum.adapter.fileio;

import com.mgu.photoalbum.exception.PhotoalbumException;

public class UnableToWriteToFilesystemException extends PhotoalbumException {

    private static final String ERROR_MESSAGE = "Unable to write to filesystem (path was %s).";

    public UnableToWriteToFilesystemException(final String path, final Throwable cause) {
        super(String.format(ERROR_MESSAGE, path), cause);
    }

    @Override
    public String getErrorCode() {
        return "G002";
    }
}