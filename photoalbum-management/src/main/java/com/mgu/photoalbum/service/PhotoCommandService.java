package com.mgu.photoalbum.service;

import java.util.List;

public interface PhotoCommandService {

    String uploadPhoto(String ownerId, String albumId, String originalFilename, String base64EncodedImage);

    void deletePhoto(String photoId);

    void replaceTags(String photoId, String tag);

    void replaceTags(String photoId, List<String> tags);
}