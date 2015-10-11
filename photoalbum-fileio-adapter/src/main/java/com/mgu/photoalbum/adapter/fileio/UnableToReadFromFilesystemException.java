package com.mgu.photoalbum.adapter.fileio;

import com.mgu.photoalbum.exception.PhotoalbumException;

/**
 * This exception is raised if the service was unable to read a requested file from
 * the filesystem. The cause for this is is potentially manifold, so the root cause
 * is retained within this exception.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
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