package com.mgu.photoalbum.service;

import com.mgu.photoalbum.domain.Album;

public interface AlbumQueryService {

    Album byId(final String id);
}