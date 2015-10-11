package com.mgu.photoalbum.service;

import com.mgu.photoalbum.domain.Album;

import java.util.List;

public interface AlbumQueryService {

    Album albumById(final String id);

    AlbumSearchResult search(final AlbumSearchRequest searchRequest);
}