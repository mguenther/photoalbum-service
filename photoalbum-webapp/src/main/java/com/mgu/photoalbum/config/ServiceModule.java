package com.mgu.photoalbum.config;

import com.google.inject.AbstractModule;
import com.mgu.photoalbum.adapter.fileio.PathAdapter;
import com.mgu.photoalbum.identity.IdGenerator;
import com.mgu.photoalbum.security.Authentication;
import com.mgu.photoalbum.security.Authorization;
import com.mgu.photoalbum.security.IdentityBasedAuth;
import com.mgu.photoalbum.service.AlbumCommandService;
import com.mgu.photoalbum.service.AlbumQueryService;
import com.mgu.photoalbum.service.AlbumService;
import com.mgu.photoalbum.service.PathScheme;
import com.mgu.photoalbum.service.PhotoCommandService;
import com.mgu.photoalbum.service.PhotoQueryService;
import com.mgu.photoalbum.service.PhotoService;
import com.mgu.photoalbum.service.scaler.ImageScaler;
import com.mgu.photoalbum.service.scaler.ScalrImageScaler;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(IdGenerator.class);

        try {
            bind(PathScheme.class).toConstructor(PathScheme.class.getConstructor(String.class));
        } catch (NoSuchMethodException e) {
            addError(e);
        }

        bind(PathAdapter.class).toInstance(new PathAdapter());

        bind(ImageScaler.class).to(ScalrImageScaler.class);
        bind(PhotoCommandService.class).to(PhotoService.class);
        bind(PhotoQueryService.class).to(PhotoService.class);
        bind(AlbumCommandService.class).to(AlbumService.class);
        bind(AlbumQueryService.class).to(AlbumService.class);

        configureSecurity();


    }

    private void configureSecurity() {
        bind(Authentication.class).to(IdentityBasedAuth.class);
        bind(Authorization.class).to(IdentityBasedAuth.class);
    }
}