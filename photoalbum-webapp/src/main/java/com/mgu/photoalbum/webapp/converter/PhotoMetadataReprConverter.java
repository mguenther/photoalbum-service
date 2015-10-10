package com.mgu.photoalbum.webapp.converter;

import com.google.inject.Inject;
import com.mgu.photoalbum.domain.Photo;
import com.mgu.photoalbum.webapp.representation.LinkRepr;
import com.mgu.photoalbum.webapp.representation.MetaRepr;
import com.mgu.photoalbum.webapp.representation.PhotoMetadataRepr;
import com.mgu.photoalbum.webapp.resource.LinkScheme;

import javax.ws.rs.HttpMethod;

public class PhotoMetadataReprConverter implements Converter<Photo, PhotoMetadataRepr> {

    private final LinkScheme linkScheme;

    @Inject
    public PhotoMetadataReprConverter(final LinkScheme linkScheme) {
        this.linkScheme = linkScheme;
    }

    @Override
    public PhotoMetadataRepr convert(final Photo photo) {

        return PhotoMetadataRepr
                .create()
                .meta(
                        MetaRepr
                                .create()
                                .link(
                                        LinkRepr
                                                .create()
                                                .href(linkScheme.toAlbum(photo.getAlbumId()))
                                                .method(HttpMethod.GET)
                                                .relation("listAlbum")
                                                .build()
                                )
                                .link(
                                        LinkRepr
                                                .create()
                                                .href(linkScheme.toPhoto(photo.getAlbumId(), photo.getId()))
                                                .method(HttpMethod.GET)
                                                .relation("viewPhoto")
                                                .mediaType("image/jpeg")
                                                .build()
                                )
                                .link(
                                        LinkRepr
                                                .create()
                                                .href(linkScheme.toPhoto(photo.getAlbumId(), photo.getId()))
                                                .method(HttpMethod.GET)
                                                .relation("deletePhoto")
                                                .build()
                                )
                                .link(
                                        LinkRepr
                                                .create()
                                                .href(linkScheme.toPhoto(photo.getAlbumId(), photo.getId()))
                                                .method(HttpMethod.PUT)
                                                .relation("updateMetadata")
                                                .build()
                                )
                                .build()
                )
                .description(photo.getDescription())
                .tag(photo.getTags())
                .build();
    }
}