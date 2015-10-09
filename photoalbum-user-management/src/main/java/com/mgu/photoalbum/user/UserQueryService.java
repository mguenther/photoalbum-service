package com.mgu.photoalbum.user;

import com.google.common.base.Optional;

/**
 * Query interface for all things related to {@link User} documents.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public interface UserQueryService {

    /**
     * Yields an {@link Optional} containing the {@link User} document that is
     * identified by the given <code>userId</code>. If no such {@link User} exists,
     * the {@link Optional} does not carry any value.
     *
     * @param userId
     *      Identifies a {@link User} document
     * @return
     *      {@link Optional} containing the {@link User} document
     */
    Optional<User> getUserById(String userId);
}