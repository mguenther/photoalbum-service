package com.mgu.photoalbum.adapter.fileio;

import com.mgu.photoalbum.exception.PhotoalbumException;

/**
 * This exception is raised when a Base64-decoding wrapper on an {@code InputStream} is unable
 * to decode the stream data properly.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class UnableToDecodeFromBase64Exception extends PhotoalbumException {

    private static final String ERROR_MESSAGE = "Unable to decode given Base64-encoded data.";

    public UnableToDecodeFromBase64Exception(final Throwable cause) {
        super(ERROR_MESSAGE, cause);
    }

    @Override
    public String getErrorCode() {
        return "G004";
    }
}