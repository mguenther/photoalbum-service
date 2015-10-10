package com.mgu.photoalbum.converter;

import com.google.inject.Inject;
import com.mgu.photoalbum.domain.Album;
import com.mgu.photoalbum.representation.AlbumShortRepr;
import com.mgu.photoalbum.representation.LinkRepr;
import com.mgu.photoalbum.resource.LinkScheme;

import javax.ws.rs.HttpMethod;

public class AlbumShortReprConverter implements Converter<Album, AlbumShortRepr> {

    private final LinkScheme linkScheme;

    @Inject
    public AlbumShortReprConverter(final LinkScheme linkScheme) {
        this.linkScheme = linkScheme;
    }

    @Override
    public AlbumShortRepr convert(final Album album) {

        return AlbumShortRepr
                .create()
                .id(album.getId())
                .title(album.getTitle())
                .numberOfPhotos(album.getContainingPhotos().size())
                .link(
                        LinkRepr
                                .create()
                                .href(linkScheme.toAlbum(album.getId()))
                                .method(HttpMethod.GET)
                                .relation("listAlbum")
                                .build()
                )
                .link(
                        LinkRepr
                                .create()
                                .href(linkScheme.toAlbum(album.getId()))
                                .method(HttpMethod.DELETE)
                                .relation("deleteAlbum")
                                .build()
                )
                .build();
    }
}