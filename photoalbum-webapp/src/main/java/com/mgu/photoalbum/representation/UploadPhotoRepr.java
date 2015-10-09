package com.mgu.photoalbum.representation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadPhotoRepr {

    private final String base64EncodedImage;

    private final String originalFilename;

    @JsonCreator
    public UploadPhotoRepr(
            @JsonProperty("base64EncodedImage") String base64EncodedImage,
            @JsonProperty("originalFilename") String originalFilename) {
        this.base64EncodedImage = base64EncodedImage;
        this.originalFilename = originalFilename;
    }

    public String getBase64EncodedImage() {
        return base64EncodedImage;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }
}