package com.mgu.photoalbum.webapp.converter;

import com.google.inject.Inject;
import com.mgu.photoalbum.domain.Album;
import com.mgu.photoalbum.service.AlbumSearchResult;
import com.mgu.photoalbum.webapp.representation.AlbumShortRepr;
import com.mgu.photoalbum.webapp.representation.GalleryRepr;
import com.mgu.photoalbum.webapp.representation.LinkRepr;
import com.mgu.photoalbum.webapp.representation.MetaRepr;
import com.mgu.photoalbum.webapp.resource.LinkScheme;

import javax.ws.rs.HttpMethod;
import java.util.List;
import java.util.stream.Collectors;

public class GalleryReprConverter implements Converter<AlbumSearchResult, GalleryRepr> {

    private final LinkScheme linkScheme;

    private final AlbumShortReprConverter albumConverter;

    @Inject
    public GalleryReprConverter(final LinkScheme linkScheme, final AlbumShortReprConverter albumConverter) {
        this.linkScheme = linkScheme;
        this.albumConverter = albumConverter;
    }

    @Override
    public GalleryRepr convert(final AlbumSearchResult searchResult) {

        final List<AlbumShortRepr> albumShortReprs = searchResult.getHits()
                .stream()
                .map(albumConverter::convert)
                .collect(Collectors.toList());

        return GalleryRepr
                .create()
                .meta(
                        MetaRepr
                                .create()
                                .link(
                                        LinkRepr
                                                .create()
                                                .href(linkScheme.toGallery())
                                                .method(HttpMethod.GET)
                                                .relation("listAlbums")
                                                .build()
                                )
                                .link(
                                        LinkRepr
                                                .create()
                                                .href(linkScheme.toGallery())
                                                .method(HttpMethod.POST)
                                                .relation("createAlbum")
                                                .build()
                                )
                                .build()
                )
                .album(albumShortReprs)
                .build();
    }
}
