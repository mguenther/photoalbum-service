package com.mgu.photoalbum.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class provides the means to construct {@link Path} objects that target
 * either an original image or its associated thumbnail image.
 *
 * This path scheme abstraction expects that the image archive follows the
 * directory structure illustrated underneath.
 * <pre>
 * {photoArchive}
 * ├── {ownerId}
 * │   ├── {albumId}
 * │   │   │ 0
 * │   │   │ ├── 7
 * │   │   │ │   ├── 07a103f416424e_original.jpg
 * │   │   │ │   ├── 07a103f416424e_thumbnail.jpg
 * </pre>
 *
 * This directory layout provides a tradeoff between the amount of images per subfolder
 * on album-level and lookup / listing speed. The first two characters of the photo ID
 * are taken in order to identify the 1st and 2nd level folders at an album folder.
 *
 * @author Markus Guenther (markus.guenther@gmail.com)
 */
public class PathScheme {

    public static final String EXTENSION = "jpg";

    public static final String SUFFIX_ORIGINAL_IMAGE = "original";

    public static final String SUFFIX_THUMBNAIL_IMAGE = "thumbnail";

    private final String pathToArchive;

    @Inject
    public PathScheme(@Named("pathToArchive") final String pathToArchive) {
        this.pathToArchive = pathToArchive;
    }

    public Path constructPathToAlbumFolder(final String ownerId, final String albumId) {
        final String relativePath = new StringBuilder()
                .append(ownerId)
                .append(File.separator)
                .append(albumId)
                .toString();
        return Paths.get(pathToArchive, relativePath);
    }

    public Path constructPathToPhotoFolder(final String ownerId, final String albumId, final String photoId) {
        final String relativePath = new StringBuilder()
                .append(ownerId)
                .append(File.separator)
                .append(albumId)
                .append(File.separator)
                .append(photoId.charAt(0))
                .append(File.separator)
                .append(photoId.charAt(1))
                .toString();
        return Paths.get(pathToArchive, relativePath);
    }

    public Path constructPathToOriginal(final String ownerId, final String albumId, final String photoId) {
        return constructPathToImage(ownerId, albumId, photoId, SUFFIX_ORIGINAL_IMAGE);
    }

    public Path constructPathToThumbnail(final String ownerId, final String albumId, final String photoId) {
        return constructPathToImage(ownerId, albumId, photoId, SUFFIX_THUMBNAIL_IMAGE);
    }

    private Path constructPathToImage(final String ownerId, final String albumId, final String photoId, final String suffix) {
        final String relativePath = new StringBuilder()
                .append(ownerId)
                .append(File.separator)
                .append(albumId)
                .append(File.separator)
                .append(photoId.charAt(0))
                .append(File.separator)
                .append(photoId.charAt(1))
                .append(File.separator)
                .append(photoId)
                .append("_")
                .append(suffix)
                .append(".")
                .append(EXTENSION)
                .toString();
        return Paths.get(pathToArchive, relativePath);
    }
}