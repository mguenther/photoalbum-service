package com.mgu.photoalbum.service;

import com.mgu.photoalbum.domain.Photo;

import java.util.List;

public interface PhotoQueryService {

    byte[] originalById(String id);

    byte[] thumbnailById(String id);

    Photo photoById(String id);

    List<Photo> search(String albumId, List<String> tags, int offset, int pageSize);
}