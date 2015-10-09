package com.mgu.photoalbum.security;

import com.google.common.base.Optional;

/**
 * Provides the means to authenticate a user based on given credentials.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public interface Authentication {

    /**
     * Uses the given {@link Credentials} to authenticate the user within the
     * photoalbum service. If the authentication fails, the resulting {@link Optional}
     * will carry no value. If it succeeds, the {@link Optional} will wrap an
     * instance of {@link Principal}, which is an injectable POJO that can be
     * used in resource method signatures (cf. <code>Auth</code> annotation).
     *
     * @param credentials
     *      contains user-supplied credentials which are used to authenticate
     *      the user
     * @return
     *      {@link Optional} containing an instance of {@link Principal}
     */
    Optional<Principal> authenticate(Credentials credentials);
}