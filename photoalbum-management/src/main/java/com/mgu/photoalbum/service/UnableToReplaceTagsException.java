package com.mgu.photoalbum.service;

import com.mgu.photoalbum.exception.PhotoalbumException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class UnableToReplaceTagsException extends PhotoalbumException {

    private static final String ERROR_MESSAGE = "Unable to replace existing tags by %s for photo with ID %s.";

    public UnableToReplaceTagsException(final String photoId, final List<String> tags) {
        super(String.format(ERROR_MESSAGE, photoId, StringUtils.join(tags, ",")));
    }

    @Override
    public String getErrorCode() {
        return "P003";
    }
}