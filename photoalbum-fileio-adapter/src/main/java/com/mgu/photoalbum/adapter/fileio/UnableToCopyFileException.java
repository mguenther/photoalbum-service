package com.mgu.photoalbum.adapter.fileio;

import com.mgu.photoalbum.exception.PhotoalbumException;

/**
 * This exception is raised whenever a copy operation to the filesystem (from any I/O resource) or to a
 * different location on the filesystem fails. Causes for this are manifold. Thus, the root cause is retained
 * with this exception.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
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