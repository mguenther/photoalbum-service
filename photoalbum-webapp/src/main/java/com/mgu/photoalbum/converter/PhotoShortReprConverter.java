package com.mgu.photoalbum.converter;

import com.google.inject.Inject;
import com.mgu.photoalbum.domain.Photo;
import com.mgu.photoalbum.representation.LinkRepr;
import com.mgu.photoalbum.representation.PhotoShortRepr;
import com.mgu.photoalbum.resource.LinkScheme;

import javax.ws.rs.HttpMethod;

public class PhotoShortReprConverter implements Converter<Photo, PhotoShortRepr> {

    private final LinkScheme linkScheme;

    @Inject
    public PhotoShortReprConverter(final LinkScheme linkScheme) {
        this.linkScheme = linkScheme;
    }

    @Override
    public PhotoShortRepr convert(final Photo photo) {

        return PhotoShortRepr
                .create()
                .description(descriptionOf(photo))
                .tags(photo.getTags())
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
                                .href(linkScheme.toThumbnail(photo.getAlbumId(), photo.getId()))
                                .method(HttpMethod.GET)
                                .relation("viewThumbnail")
                                .mediaType("image/jpeg")
                                .build()
                )
                .link(
                        LinkRepr
                                .create()
                                .href(linkScheme.toPhoto(photo.getAlbumId(), photo.getId()))
                                .method(HttpMethod.GET)
                                .relation("viewMetadata")
                                .build()
                )
                .link(
                        LinkRepr
                                .create()
                                .href(linkScheme.toDownload(photo.getAlbumId(), photo.getId()))
                                .method(HttpMethod.GET)
                                .relation("downloadPhoto")
                                .mediaType("image/jpeg")
                                .build()
                )
                .build();
    }

    private String descriptionOf(final Photo photo) {
        return photo.getDescription().isEmpty() ? photo.getOriginalFilename() : photo.getDescription();
    }
}