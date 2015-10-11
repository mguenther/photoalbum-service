package com.mgu.photoalbum.service;

import com.mgu.photoalbum.domain.Photo;

public interface PhotoQueryService {

    byte[] originalById(String id);

    byte[] thumbnailById(String id);

    Photo photoById(String id);

    PhotoSearchResult search(PhotoSearchRequest searchQuery);
}