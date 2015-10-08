package com.mgu.photoalbum.service;

import com.google.inject.Inject;
import com.mgu.photoalbum.adapter.fileio.InputStreamAdapter;
import com.mgu.photoalbum.adapter.fileio.PathAdapter;
import com.mgu.photoalbum.domain.Photo;
import com.mgu.photoalbum.identity.IdGenerator;
import com.mgu.photoalbum.service.scaler.ImageScaler;
import com.mgu.photoalbum.storage.PhotoRepository;
import org.ektorp.UpdateConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class PhotoService implements PhotoCommandService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoService.class);

    private final PhotoRepository repository;

    private final PathScheme pathScheme;

    private final PathAdapter pathAdapter;

    private final InputStreamAdapter inputStreamAdapter;

    private final ImageScaler scaler;

    private final Supplier<String> photoIdGenerator;

    @Inject
    public PhotoService(final PhotoRepository repository, final PathScheme pathScheme, final PathAdapter pathAdapter, final InputStreamAdapter inputStreamAdapter, final IdGenerator idGenerator, final ImageScaler scaler) {
        this.repository = repository;
        this.pathAdapter = pathAdapter;
        this.inputStreamAdapter = inputStreamAdapter;
        this.pathScheme = pathScheme;
        this.scaler = scaler;
        this.photoIdGenerator = () -> idGenerator.generateId("AL", 14);
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

        final Photo photo = Photo
                .create()
                .belongsTo(albumId)
                .createdBy(ownerId)
                .id(photoId)
                .originalFilename(originalFilename)
                .pathToOriginal(originalPath.toString())
                .pathToThumbnail(thumbnailPath.toString())
                .build();
        repository.add(photo);

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
        return scaler.scale(unscaledImage, new Dimension(200, 150));
    }

    @Override
    public void deletePhoto(final String photoId) {
        if (!repository.contains(photoId)) {
            throw new PhotoDoesNotExistException(photoId);
        }
        final Photo photo = repository.get(photoId);
        final Path photoFolder = pathScheme.constructPathToPhotoFolder(photo.getOwnerId(), photo.getAlbumId(), photoId);
        pathAdapter.deleteDirectory(photoFolder);
        repository.remove(photo);
    }

    @Override
    public void replaceTags(final String photoId, final String tag) {
        replaceTags(photoId, Arrays.asList(tag));
    }

    @Override
    public void replaceTags(final String photoId, final List<String> tags) {
        if (!repository.contains(photoId)) {
            throw new PhotoDoesNotExistException(photoId);
        }
        final Photo photo = repository.get(photoId);
        photo.untag();
        photo.tag(tags);
        try {
            repository.update(photo);
        } catch (UpdateConflictException e) {
            throw new UnableToReplaceTagsException(photoId, tags);
        }
    }
}