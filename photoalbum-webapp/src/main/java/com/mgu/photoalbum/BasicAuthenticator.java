package com.mgu.photoalbum;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.mgu.photoalbum.security.Authentication;
import com.mgu.photoalbum.security.Credentials;
import com.mgu.photoalbum.security.Principal;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import javax.ws.rs.WebApplicationException;

public class BasicAuthenticator implements Authenticator<BasicCredentials, Principal> {

    private Authentication authentication;

    @Inject
    public BasicAuthenticator(final Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public Optional<Principal> authenticate(final BasicCredentials basicCredentials) throws AuthenticationException {

        final Credentials credentials = Credentials.create(
                basicCredentials.getUsername(),
                basicCredentials.getPassword());
        final Optional<Principal> principalOptional = this.authentication.authenticate(credentials);

        if (!principalOptional.isPresent()) {
            throw new WebApplicationException(401);
        }

        return principalOptional;
    }
}
