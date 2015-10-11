package com.mgu.photoalbum.adapter.fileio;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * Provides some convenience methods to handle {@link InputStream}.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class InputStreamAdapter {

    public InputStream getBase64DecodingInputStream(final String base64EncodedData) {
        try {
            return Base64.getDecoder().wrap(IOUtils.toInputStream(base64EncodedData, "UTF-8"));
        } catch (IOException e) {
            throw new UnableToDecodeFromBase64Exception(e);
        }
    }
}