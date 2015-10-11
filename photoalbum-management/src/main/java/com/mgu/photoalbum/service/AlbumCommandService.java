package com.mgu.photoalbum.service;

/**
 * {@code AlbumCommandService} provides the means to create and delete domain objects of type {@code Album}.
 * This service does not determine if a command is authorized. Clients of this class must ensure that the
 * requested command is admissible.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public interface AlbumCommandService {

    /**
     * Creates a new album with the provided metadata.
     *
     * @param ownerId
     *      represents the ID of the user that requested to create the album
     * @param title
     *      represents the title of the album
     * @return
     *      unique ID that is identifies the newly created album
     */
    String createAlbum(String ownerId, String title);

    /**
     * Deletes the album identified by the given {@code albumId}. Implementing classes must ensure
     * that this method also deletes all photos that are associated with the album. Implementing
     * classes may handle a non-existing album as good case, since the system already has reached
     * a convergent stace with regard to a missing album (this should not raise an error).
     *
     * @param albumId
     *      Uniquely identifies an album
     */
    void deleteAlbum(String albumId);
}