package com.mgu.photoalbum.service;

import java.io.InputStream;
import java.util.List;

public interface PhotoCommandService {

    String uploadPhoto(String ownerId, String albumId, String originalFilename, InputStream fileInputStream);

    void deletePhoto(String photoId);

    void deletePhotos(String albumId);

    void updateMetadata(String photoId, String description, List<String> tags);
}