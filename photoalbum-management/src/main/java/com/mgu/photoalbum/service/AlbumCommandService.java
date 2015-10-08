package com.mgu.photoalbum.service;

public interface AlbumCommandService {

    String createAlbum(String ownerId, String title);

    void deleteAlbum(String id);
}
