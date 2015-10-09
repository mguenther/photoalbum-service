package com.mgu.photoalbum.security;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.mgu.photoalbum.domain.Album;
import com.mgu.photoalbum.domain.Photo;
import com.mgu.photoalbum.user.User;
import com.mgu.photoalbum.user.UserQueryService;

public class IdentityBasedAuth implements Authentication, Authorization {

    private final UserQueryService userQueryService;

    @Inject
    public IdentityBasedAuth(final UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @Override
    public Optional<Principal> authenticate(final Credentials credentials) {

        final Optional<User> userOptional = this.userQueryService.getUserById(credentials.getUserId());

        if (userOptional.isPresent()) {
            final String otherHashedPassword = userOptional.get().getHashedPassword();
            if (credentials.matches(otherHashedPassword)) {
                return Optional.of(Principal.from(userOptional.get()));
            }
        }

        return Optional.absent();
    }

    @Override
    public boolean isAuthorized(final Principal principal, final Album album) {
        return principal.getUserId().equals(album.getOwnerId());
    }

    @Override
    public boolean isAuthorized(final Principal principal, final Photo photo) {
        return principal.getUserId().equals(photo.getOwnerId());
    }
}