package com.mgu.photoalbum.service;

import com.google.inject.Inject;
import com.mgu.photoalbum.domain.Album;
import com.mgu.photoalbum.identity.IdGenerator;
import com.mgu.photoalbum.storage.AlbumRepository;

import java.util.List;
import java.util.function.Supplier;

public class AlbumService implements AlbumCommandService, AlbumQueryService {

    private final AlbumRepository repository;

    private final Supplier<String> albumIdGenerator;

    @Inject
    public AlbumService(final AlbumRepository repository, final IdGenerator idGenerator) {
        this.repository = repository;
        this.albumIdGenerator = () -> idGenerator.generateId("AL", 14);
    }

    @Override
    public String createAlbum(final String ownerId, final String title) {
        final String uniqueAlbumId = generateUniqueAlbumId();
        final Album album = Album
                .create()
                .id(uniqueAlbumId)
                .createdBy(ownerId)
                .title(title)
                .build();
        repository.add(album);
        return uniqueAlbumId;
    }

    private String generateUniqueAlbumId() {
        String albumId = albumIdGenerator.get();
        while (repository.contains(albumId)) {
            albumId = albumIdGenerator.get();
        }
        return albumId;
    }

    @Override
    public void deleteAlbum(final String id) {
        if (!repository.contains(id)) {
            // deleting an existing album and trying to delete a non-existing album leads to the
            // same state convergence. thus, we do not treat a deletion request to a non-existing album
            // not as an error, but return immediately
            return;
        }
        final Album album = repository.get(id);
        // TODO: remove photos and files as well!
        repository.remove(album);
    }

    @Override
    public Album albumById(final String id) {
        if (!repository.contains(id)) {
            throw new AlbumDoesNotExistException(id);
        }
        final Album album = repository.get(id);
        return album;
    }

    @Override
    public List<Album> albumsByOwner(String ownerId) {
        return null;
    }
}