package com.mgu.photoalbum.service;

import com.mgu.photoalbum.exception.PhotoalbumException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class UnableToUpdateMetadataException extends PhotoalbumException {

    private static final String ERROR_MESSAGE = "Unable to set description to %s and replace existing tags by %s for photo with ID %s.";

    public UnableToUpdateMetadataException(final String photoId, final String description, final List<String> tags) {
        super(String.format(ERROR_MESSAGE, description, StringUtils.join(tags, ","), photoId));
    }

    @Override
    public String getErrorCode() {
        return "P003";
    }
}