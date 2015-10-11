package com.mgu.photoalbum.webapp.converter;

import com.google.inject.Inject;
import com.mgu.photoalbum.service.AlbumHit;
import com.mgu.photoalbum.webapp.representation.AlbumShortRepr;
import com.mgu.photoalbum.webapp.representation.LinkRepr;
import com.mgu.photoalbum.webapp.resource.LinkScheme;

import javax.ws.rs.HttpMethod;

public class AlbumShortReprConverter implements Converter<AlbumHit, AlbumShortRepr> {

    private final LinkScheme linkScheme;

    @Inject
    public AlbumShortReprConverter(final LinkScheme linkScheme) {
        this.linkScheme = linkScheme;
    }

    @Override
    public AlbumShortRepr convert(final AlbumHit albumHit) {

        final AlbumShortRepr.AlbumShortReprBuilder builder = AlbumShortRepr
                .create()
                .id(albumHit.getAlbum().getId())
                .title(albumHit.getAlbum().getTitle())
                .numberOfPhotos(albumHit.getNumberOfPhotos())
                .link(
                        LinkRepr
                                .create()
                                .href(linkScheme.toAlbum(albumHit.getAlbum().getId()))
                                .method(HttpMethod.GET)
                                .relation("listAlbum")
                                .build()
                )
                .link(
                        LinkRepr
                                .create()
                                .href(linkScheme.toAlbum(albumHit.getAlbum().getId()))
                                .method(HttpMethod.DELETE)
                                .relation("deleteAlbum")
                                .build()
                );

        if (!albumHit.getCoverPhotoId().isEmpty()) {
            builder.link(
                    LinkRepr
                            .create()
                            .href(linkScheme.toThumbnail(albumHit.getAlbum().getId(), albumHit.getCoverPhotoId()))
                            .method(HttpMethod.GET)
                            .relation("viewCover")
                            .mediaType("image/jpeg")
                            .build()
            );
        }

        return builder.build();
    }
}