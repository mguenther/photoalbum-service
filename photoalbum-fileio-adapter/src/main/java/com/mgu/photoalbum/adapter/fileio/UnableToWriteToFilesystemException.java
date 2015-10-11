package com.mgu.photoalbum.adapter.fileio;

import com.mgu.photoalbum.exception.PhotoalbumException;

/**
 * This exception is raised if the service was unable to write a given file
 * to the filesystem. The cause for this is potentially manifold, so the root
 * cause is retained with this exception.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
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