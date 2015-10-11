package com.mgu.photoalbum.service;

import com.mgu.photoalbum.domain.Photo;

/**
 * {@code PhotoQueryService} provides the means to lookup {@link Photo} documents by their ID,
 * search for photos given a set of search parameters and to access the binary representation of
 * a photo, both in its original and scaled-down (thumbnail) form.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public interface PhotoQueryService {

    /**
     * Returns the byte data (JPEG) of the original photo identified by the given ID.
     *
     * @param photoId
     *      Uniquely identifies a photo within the system
     * @throws PhotoDoesNotExistException
     *      in case the ID refers a non-existing photo
     * @throws ImageDoesNotExistException
     *      in case the associated original photo file in the image archive does not exist
     * @return
     *      {@code byte[]} which holds the raw binary image data encoded as JPEG
     *
     */
    byte[] originalById(String photoId);

    /**
     * Returns the byte data (JPEG) of the thumbnail identified by the given ID.
     *
     * @param photoId
     *      Uniquely identifies a photo within the system
     * @throws PhotoDoesNotExistException
     *      in case the ID refers to a non-existing photo
     * @throws ImageDoesNotExistException
     *      in case the associated thumbnail file in the image archive does not exist
     * @return
     *      {@code byte[]} which holds the raw binary image data encoded as JPEG
     */
    byte[] thumbnailById(String photoId);

    /**
     * Retrieves the {@link Photo} identified by the given ID.
     *
     * @param photoId
     *      Uniquely identifies an {@link Photo} within the system
     * @throws PhotoDoesNotExistException
     *      in case the ID refers a non-existing photo
     * @return
     *      {@link Photo} identified by the given ID
     */
    Photo photoById(String photoId);

    /**
     * Performs a search request using the provided search parameters encoded with
     * {@link PhotoSearchRequest}.
     *
     * @param searchRequest
     *      data holder which is used to parameterize the search for photos
     * @return
     *      Instance of {@link PhotoSearchResult} which holds the search results
     *      and retains the search parameters that led to those results
     */
    PhotoSearchResult search(PhotoSearchRequest searchRequest);
}