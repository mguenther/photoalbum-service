package com.mgu.photoalbum.service;

import com.google.inject.Inject;
import com.mgu.photoalbum.domain.Album;
import com.mgu.photoalbum.identity.IdGenerator;
import com.mgu.photoalbum.storage.AlbumRepository;
import org.ektorp.UpdateConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class AlbumService implements AlbumCommandService, AlbumQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumService.class);

    private final AlbumRepository repository;

    private PhotoCommandService photoCommandService;

    private final Supplier<String> albumIdGenerator;

    @Inject
    public AlbumService(
            final AlbumRepository repository,
            final PhotoCommandService photoCommandService,
            final IdGenerator idGenerator) {
        this.repository = repository;
        this.photoCommandService = photoCommandService;
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
    public void deleteAlbum(final String albumId) {
        if (!repository.contains(albumId)) {
            // deleting an existing album and trying to delete a non-existing album leads to the
            // same state convergence. thus, we do not treat a deletion request to a non-existing album
            // not as an error, but return immediately
            return;
        }
        final Album album = repository.get(albumId);
        album.getContainingPhotos().forEach(photoId -> photoCommandService.deletePhoto(photoId));
        repository.remove(album);
        LOGGER.info("Removed album with ID " + albumId + " along with all associated photos.");
    }

    @Override
    public void removePhotoFromAlbum(String albumId, String photoId) {
        if (!repository.contains(albumId)) {
            return;
        }
        final Album album = repository.get(albumId);
        album.dissociatePhoto(photoId);
        try {
            repository.update(album);
        } catch (UpdateConflictException e) {
            throw new UnableToUpdateAlbumException(albumId, e);
        }
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
    public List<Album> albumsByOwner(final String ownerId) {
        final List<Album> albumsByOwner = new ArrayList<>();
        albumsByOwner.addAll(repository.getAllByOwner(ownerId));
        return Collections.unmodifiableList(albumsByOwner);
    }
}