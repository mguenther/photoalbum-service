package com.mgu.photoalbum.service;

import com.google.inject.Inject;
import com.mgu.photoalbum.adapter.fileio.PathAdapter;
import com.mgu.photoalbum.domain.Photo;
import com.mgu.photoalbum.identity.IdGenerator;
import com.mgu.photoalbum.service.scaler.ImageScaler;
import com.mgu.photoalbum.storage.PhotoRepository;
import org.apache.commons.lang3.StringUtils;
import org.ektorp.UpdateConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PhotoService implements PhotoCommandService, PhotoQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoService.class);

    private final PhotoRepository repository;

    private final AlbumCommandService albumCommandService;

    private final PathScheme pathScheme;

    private final PathAdapter pathAdapter;

    private final ImageScaler scaler;

    private final Supplier<String> photoIdGenerator;

    @Inject
    public PhotoService(
            final AlbumCommandService albumCommandService,
            final PhotoRepository repository,
            final PathScheme pathScheme,
            final PathAdapter pathAdapter,
            final IdGenerator idGenerator,
            final ImageScaler scaler) {
        this.repository = repository;
        this.albumCommandService = albumCommandService;
        this.pathAdapter = pathAdapter;
        this.pathScheme = pathScheme;
        this.scaler = scaler;
        this.photoIdGenerator = () -> idGenerator.generateId(14);
    }

    @Override
    public String uploadPhoto(
            final String ownerId,
            final String albumId,
            final String originalFilename,
            final InputStream fileInputStream) {

        final String photoId = generateUniquePhotoId();

        Path photoFolderPath = pathScheme.constructPathToPhotoFolder(ownerId, albumId, photoId);
        Path originalPath = pathScheme.constructPathToOriginal(ownerId, albumId, photoId);
        Path thumbnailPath = pathScheme.constructPathToThumbnail(ownerId, albumId, photoId);

        pathAdapter.createDirectory(photoFolderPath);
        pathAdapter.copy(fileInputStream, originalPath);
        pathAdapter.copyImage(generateThumbnail(originalPath), thumbnailPath);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Generated thumbnail for photo with ID " + photoId + " at " + thumbnailPath.toString());
        }

        final Photo photo = Photo
                .create()
                .belongsTo(albumId)
                .createdBy(ownerId)
                .id(photoId)
                .originalFilename(originalFilename)
                .build();
        repository.add(photo);

        LOGGER.info("Successfully uploaded photo with ID " + photoId + " to repository.");

        return photoId;
    }

    private String generateUniquePhotoId() {
        String photoId = photoIdGenerator.get();
        while (repository.contains(photoId)) {
            photoId = photoIdGenerator.get();
        }
        return photoId;
    }

    private BufferedImage generateThumbnail(final Path pathToUnscaledImage) {
        final byte[] unscaledImage = pathAdapter.readBytes(pathToUnscaledImage);
        return scaler.scale(unscaledImage, new Dimension(550, 370));
    }

    @Override
    public void deletePhotos(final String albumId) {
        repository.getAllByAlbum(albumId).forEach(photo -> deletePhoto(photo.getId()));
    }

    @Override
    public void deletePhoto(final String photoId) {
        if (!repository.contains(photoId)) {
            // we are already in converged state, so no action is taken
            LOGGER.debug("Received a delete command for a non-existing photo (ID " + photoId + ").");
            return;
        }
        final Photo photo = repository.get(photoId);
        deletePhotoFiles(photo.getOwnerId(), photo.getAlbumId(), photoId);
        repository.remove(photo);

        LOGGER.info("Removed photo with ID " + photoId + " from photo album with ID " + photo.getAlbumId() + ".");
    }

    private void deletePhotoFiles(final String ownerId, final String albumId, final String photoId) {
        final File original = pathScheme.constructPathToOriginal(ownerId, albumId, photoId).toFile();
        final File thumbnail = pathScheme.constructPathToThumbnail(ownerId, albumId, photoId).toFile();
        if (pathAdapter.delete(original)) {
            LOGGER.info("Deleted original photo at " + original.toString());
        }
        if (pathAdapter.delete(thumbnail)) {
            LOGGER.info("Deleted thumbnail photo at " + thumbnail.toString());
        }
    }

    @Override
    public void updateMetadata(final String photoId, final String description, final List<String> tags) {
        if (!repository.contains(photoId)) {
            throw new PhotoDoesNotExistException(photoId);
        }
        final Photo photo = repository.get(photoId);
        photo.describe(description);
        photo.untag();
        photo.tag(tags);
        try {
            repository.update(photo);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Updated metadata for photo with ID " + photoId + " to: description='" + description + "', " +
                            "tags='" + StringUtils.join(tags, ",") + "'.");
            }
        } catch (UpdateConflictException e) {
            throw new UnableToUpdateMetadataException(photoId, description, tags);
        }
    }

    @Override
    public byte[] originalById(final String photoId) {

        if (!repository.contains(photoId)) {
            throw new PhotoDoesNotExistException(photoId);
        }

        final Photo photo = repository.get(photoId);
        final Path pathToOriginal = pathScheme.constructPathToOriginal(photo.getOwnerId(), photo.getAlbumId(), photoId);

        if (!pathAdapter.exists(pathToOriginal)) {
            throw new ImageDoesNotExistException(photoId);
        }

        return pathAdapter.readBytes(pathToOriginal);
    }

    @Override
    public byte[] thumbnailById(final String photoId) {

        if (!repository.contains(photoId)) {
            throw new PhotoDoesNotExistException(photoId);
        }

        final Photo photo = repository.get(photoId);
        final Path pathToThumbnail = pathScheme.constructPathToThumbnail(photo.getOwnerId(), photo.getAlbumId(), photoId);

        if (!pathAdapter.exists(pathToThumbnail)) {
            throw new ImageDoesNotExistException(photoId);
        }

        return pathAdapter.readBytes(pathToThumbnail);
    }

    @Override
    public Photo photoById(final String photoId) {
        if (!repository.contains(photoId)) {
            throw new PhotoDoesNotExistException(photoId);
        }
        return repository.get(photoId);
    }

    @Override
    public PhotoSearchResult search(final PhotoSearchRequest searchRequest) {
        final List<Photo> photosInAlbum = repository.getAllByAlbum(searchRequest.getAlbumId());
        final List<Photo> filteredPhotos = photosInAlbum
                .stream()
                .filter(photo -> photo.anyTagMatches(searchRequest.getTags()))
                .skip(searchRequest.getOffset())
                .limit(searchRequest.getPageSize())
                .collect(Collectors.toList());
        return new PhotoSearchResult(searchRequest, photosInAlbum.size(), filteredPhotos);
    }
}