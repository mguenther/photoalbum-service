package com.mgu.photoalbum.service;

import java.io.InputStream;
import java.util.List;

/**
 * {@code PhotoCommandService} provides the means to upload photos to existing photo albums, delete those photos
 * and update their metadata. This service does not determine if a command is authorized. Clients of this class
 * must ensure that the requested command is admissible.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public interface PhotoCommandService {

    /**
     * Ingests the image content with filename {@code originalFilename} and turns the image into
     * a managed photo resource. The photo resource is associated with an owner and an album it belongs to.
     *
     * @param ownerId
     *      Uniquely identifies a user within the system
     * @param albumId
     *      Uniquely identifies an album within the system
     * @param originalFilename
     *      Represents the original filename of the uploaded photo
     * @param fileInputStream
     *      Holds the binary data of the photo (JPEG)
     * @return
     *      Unique ID of the newly created photo resource
     */
    String uploadPhoto(String ownerId, String albumId, String originalFilename, InputStream fileInputStream);

    /**
     * Deletes the photo identified by the given ID.
     *
     * @param photoId
     *      Uniquely identifies a photo within the system
     */
    void deletePhoto(String photoId);

    /**
     * Deletes all photos that belong to the given album (identified by its ID).
     *
     * @param albumId
     *      Uniquely identifies an album within the system
     */
    void deletePhotos(String albumId);

    /**
     * Updates the metadata of the photo identified by the given ID with the provided {@code description}
     * and the provided new list of {@code tags}.
     *
     * @param photoId
     *      Uniquely identifies a photo within the system
     * @param description
     *      Refers to the description that replaces the old description
     * @param tags
     *      List of tags that replaces the old list of tags
     * @throws PhotoDoesNotExistException
     *      in case the referenced photo does not exist
     * @throws UnableToUpdateMetadataException
     *      in case there was a concurrent update and the client based its changes off of an
     *      old version of the resp. photo
     */
    void updateMetadata(String photoId, String description, List<String> tags);
}