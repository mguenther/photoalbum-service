package com.mgu.photoalbum.service;

import com.mgu.photoalbum.domain.Photo;

public interface PhotoQueryService {

    byte[] originalById(String id); // byte[] originalById(PhotoId identity)

    byte[] thumbnailById(String id);

    Photo byId(String id);
}