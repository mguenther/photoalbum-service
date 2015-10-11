package com.mgu.photoalbum.service;

import com.mgu.photoalbum.domain.Album;

import java.util.List;

/**
 * {@code AlbumQueryService} provides the means to lookup {@link Album}s by their ID or search for them.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public interface AlbumQueryService {

    /**
     * Retrieves the {@link Album} identified by the given ID.
     *
     * @param albumId
     *      Uniquely identifies an {@link Album} within the system
     * @throws AlbumDoesNotExistException
     *      in case the ID refers to a non-existing album
     * @return
     *      {@link Album} identified by the given ID
     */
    Album albumById(final String albumId);

    /**
     * Performs a search request using the provided search parameters encoded with
     * {@link AlbumSearchRequest}.
     *
     * @param searchRequest
     *      data holder which is used to parameterize the search for albums
     * @return
     *      Instance of {@link AlbumSearchResult} which holds the search results
     *      and retains the search parameters that led to those results
     */
    AlbumSearchResult search(final AlbumSearchRequest searchRequest);
}