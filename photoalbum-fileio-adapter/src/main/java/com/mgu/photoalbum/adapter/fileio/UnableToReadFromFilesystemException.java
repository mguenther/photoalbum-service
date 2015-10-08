package com.mgu.photoalbum.adapter.fileio;

import com.mgu.photoalbum.exception.PhotoalbumException;

public class UnableToReadFromFilesystemException extends PhotoalbumException {

    private static final String ERROR_MESSAGE = "Unable to read the contents of %s.";

    public UnableToReadFromFilesystemException(final String path, final Throwable cause) {
        super(String.format(ERROR_MESSAGE, path), cause);
    }

    @Override
    public String getErrorCode() {
        return "G001";
    }
}