package com.mgu.photoalbum.security;

/**
 * Provides the means to authorize an already authenticated user (cf. {@link Principal})
 * for accessing managed content within the photoalbum service.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public interface Authorization {

    /**
     * Determines whether the given {@link Principal} is allowed to access content that is
     * associated with the owner ID given by <code>ownerId</code>.
     *
     * Content like albums and photos is always associated with an owner, where an owner is a
     * regular user within the system. The authenticated user (cf. {@link Principal}) is allowed to
     * access this content if one of the following conditions evaluates to true (please bear
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
     * @param ownerId
     *      represents the owner ID for which the client needs to determine whether the
     *      authenticated user is allowed to access content that is associated with that owner ID
     * @return
     *      <code>true</code> if the authenticated user is authorized, <code>false</code> otherwise
     */
    boolean isAuthorized(Principal principal, String ownerId);
}