package com.mgu.photoalbum.service;

import com.google.inject.Inject;
import com.mgu.photoalbum.adapter.fileio.InputStreamAdapter;
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

    private final InputStreamAdapter inputStreamAdapter;

    private final ImageScaler scaler;

    private final Supplier<String> photoIdGenerator;

    @Inject
    public PhotoService(
            final AlbumCommandService albumCommandService,
            final PhotoRepository repository,
            final PathScheme pathScheme,
            final PathAdapter pathAdapter,
            final InputStreamAdapter inputStreamAdapter,
            final IdGenerator idGenerator,
            final ImageScaler scaler) {
        this.repository = repository;
        this.albumCommandService = albumCommandService;
        this.pathAdapter = pathAdapter;
        this.inputStreamAdapter = inputStreamAdapter;
        this.pathScheme = pathScheme;
        this.scaler = scaler;
        this.photoIdGenerator = () -> idGenerator.generateId(14);
    }

    @Override
    public String uploadPhoto(
            final String ownerId,
            final String albumId,
            final String originalFilename,
            final String base64EncodedImage) {

        final String photoId = generateUniquePhotoId();

        Path photoFolderPath = pathScheme.constructPathToPhotoFolder(ownerId, albumId, photoId);
        Path originalPath = pathScheme.constructPathToOriginal(ownerId, albumId, photoId);
        Path thumbnailPath = pathScheme.constructPathToThumbnail(ownerId, albumId, photoId);

        pathAdapter.createDirectory(photoFolderPath);
        pathAdapter.copy(inputStreamAdapter.getBase64DecodingInputStream(base64EncodedImage), originalPath);
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
    public void deletePhoto(final String photoId) {
        if (!repository.contains(photoId)) {
            throw new PhotoDoesNotExistException(photoId);
        }
        final Photo photo = repository.get(photoId);
        deletePhotoFiles(photo.getOwnerId(), photo.getAlbumId(), photoId);
        repository.remove(photo);

        LOGGER.info("Removed photo with ID " + photoId + " from photo album with ID " + photo.getAlbumId() + ".");
        // TODO (mgu): Should not be necessary and we need to get rid of that dependency!
        albumCommandService.removePhotoFromAlbum(photo.getAlbumId(), photoId);
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
    public List<Photo> search(final String albumId, final List<String> tags, final int offset, final int pageSize) {
        return repository
                .getAllByAlbum(albumId)
                .stream()
                .filter(photo -> photo.anyTagMatches(tags))
                .skip(offset)
                .limit(pageSize)
                .collect(Collectors.toList());
    }
}