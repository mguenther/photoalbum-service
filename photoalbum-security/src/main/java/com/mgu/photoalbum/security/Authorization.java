package com.mgu.photoalbum.security;

import com.mgu.photoalbum.domain.Album;
import com.mgu.photoalbum.domain.Photo;

/**
 * Provides the means to authorize an already authenticated user (cf. {@link Principal})
 * for accessing managed content within the photoalbum service.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public interface Authorization {

    /**
     * Determines whether the given {@link Principal} is allowed to access an {@link Album} that is
     * associated with the owner ID given by <code>ownerId</code>.
     *
     * An {@link Album} is always associated with an owner, where an owner is a
     * regular user within the system. The authenticated user (cf. {@link Principal}) is allowed to
     * access the {@link Album} if one of the following conditions evaluates to true (please bear
     * in mind that this component has no notion of what content is addressed; access rules are
     * solely based on matching user IDs (for an authenticated user) to owner IDs):
     *
     * <ul>
     *     <li>The ID of the authenticated user is the same as the owner ID associated with
     *     the requested content</li>
     * </ul>
     *
     * Every other case *must* evaluate to false.
     *
     * @param principal
     *      represents an authenticated user within the photoalbum service
     * @param album
     *      represents the content (cf. {@link Album}) the authenticated requests to access
     * @return
     *      <code>true</code> if the authenticated user is authorized, <code>false</code> otherwise
     */
    boolean isAuthorized(Principal principal, Album album);

    /**
     * Determines whether the given {@link Principal} is allowed to access a {@link Photo} that is
     * associated with the owner ID given by <code>ownerId</code>.
     *
     * A {@link Photo} is always associated with an owner, where an owner is a
     * regular user within the system. The authenticated user (cf. {@link Principal}) is allowed to
     * access the {@link Photo} if one of the following conditions evaluates to true (please bear
     * in mind that this component has no notion of what content is addressed; access rules are
     * solely based on matching user IDs (for an authenticated user) to owner IDs):
     *
     * <ul>
     *     <li>The ID of the authenticated user is the same as the owner ID associated with
     *     the requested content</li>
     * </ul>
     *
     * Every other case *must* evaluate to false.
     *
     * @param principal
     *      represents an authenticated user within the photoalbum service
     * @param photo
     *      represents the content (cf. {@link Photo}) the authenticated requests to access
     * @return
     *      <code>true</code> if the authenticated user is authorized, <code>false</code> otherwise
     */
    boolean isAuthorized(Principal principal, Photo photo);
}