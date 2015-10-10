package com.mgu.photoalbum.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
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
import com.mgu.photoalbum.storage.AlbumRepository;
import com.mgu.photoalbum.storage.PhotoRepository;
import com.mgu.photoalbum.user.UserQueryService;
import com.mgu.photoalbum.user.UserRepository;
import com.mgu.photoalbum.user.UserService;
import com.mgu.photoalbum.webapp.converter.AlbumReprConverter;
import com.mgu.photoalbum.webapp.converter.AlbumShortReprConverter;
import com.mgu.photoalbum.webapp.converter.GalleryReprConverter;
import com.mgu.photoalbum.webapp.converter.PhotoMetadataReprConverter;
import com.mgu.photoalbum.webapp.converter.PhotoShortReprConverter;
import com.mgu.photoalbum.webapp.resource.LinkScheme;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import javax.inject.Named;

public class ServiceModule extends AbstractModule {

    @Provides
    @Named("pathToArchive")
    public String provideImageArchive(final ServiceConfig configuration) {
        return configuration.getPhotoalbumConfig().getImageArchivePath();
    }

    @Provides
    public CouchDbConnector provideConnection(final ServiceConfig config) {
        final DatabaseConfig database = config.getDatabaseConfig();
        try {
            final HttpClient httpClient = new StdHttpClient.Builder()
                    .url(database.getUrl())
                    .connectionTimeout(10000)
                    .socketTimeout(10000)
                    .build();
            final CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
            return new StdCouchDbConnector(database.getName(), dbInstance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void configure() {

        bind(IdGenerator.class);

        try {
            bind(PathScheme.class).toConstructor(PathScheme.class.getConstructor(String.class));
        } catch (NoSuchMethodException e) {
            addError(e);
        }

        bind(PathAdapter.class).toInstance(new PathAdapter());

        configureAssetManagement();
        configureUserManagement();
        configureSecurity();
        configureFacade();
    }

    private void configureAssetManagement() {
        bind(ImageScaler.class).to(ScalrImageScaler.class);
        bind(PhotoCommandService.class).to(PhotoService.class);
        bind(PhotoQueryService.class).to(PhotoService.class);
        bind(AlbumCommandService.class).to(AlbumService.class);
        bind(AlbumQueryService.class).to(AlbumService.class);
        try {
            bind(AlbumRepository.class).toConstructor(AlbumRepository.class.getConstructor(CouchDbConnector.class));
            bind(PhotoRepository.class).toConstructor(PhotoRepository.class.getConstructor(CouchDbConnector.class));
        } catch (NoSuchMethodException e) {
            addError(e);
        }
    }

    private void configureUserManagement() {
        bind(UserQueryService.class).to(UserService.class);
        try {
            bind(UserRepository.class).toConstructor(UserRepository.class.getConstructor(CouchDbConnector.class));
        } catch (NoSuchMethodException e) {
            addError(e);
        }
    }

    private void configureSecurity() {
        bind(Authentication.class).to(IdentityBasedAuth.class);
        bind(Authorization.class).to(IdentityBasedAuth.class);
    }

    private void configureFacade() {
        bind(LinkScheme.class);
        bind(AlbumReprConverter.class);
        bind(AlbumShortReprConverter.class);
        bind(GalleryReprConverter.class);
        bind(PhotoMetadataReprConverter.class);
        bind(PhotoShortReprConverter.class);
    }
}