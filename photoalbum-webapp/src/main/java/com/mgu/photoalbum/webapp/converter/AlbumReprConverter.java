package com.mgu.photoalbum.webapp.converter;

import com.google.inject.Inject;
import com.mgu.photoalbum.domain.Album;
import com.mgu.photoalbum.service.PhotoSearchRequest;
import com.mgu.photoalbum.service.PhotoSearchResult;
import com.mgu.photoalbum.webapp.representation.AlbumRepr;
import com.mgu.photoalbum.webapp.representation.LinkRepr;
import com.mgu.photoalbum.webapp.representation.MetaRepr;
import com.mgu.photoalbum.webapp.representation.PhotoShortRepr;
import com.mgu.photoalbum.webapp.resource.LinkScheme;

import javax.ws.rs.HttpMethod;
import java.util.List;
import java.util.stream.Collectors;

public class AlbumReprConverter {

    private final LinkScheme linkScheme;

    private final PhotoShortReprConverter photoConverter;

    @Inject
    public AlbumReprConverter(final LinkScheme linkScheme, final PhotoShortReprConverter photoConverter) {
        this.linkScheme = linkScheme;
        this.photoConverter = photoConverter;
    }

    public AlbumRepr convert(final Album album, final PhotoSearchRequest searchQuery, final PhotoSearchResult searchResult) {

        final List<PhotoShortRepr> photoShortReprs = searchResult
                .getHits()
                .stream()
                .map(photoConverter::convert)
                .collect(Collectors.toList());

        return AlbumRepr.
                create()
                .meta(
                        MetaRepr
                                .create()
                                .link(
                                        LinkRepr
                                                .create()
                                                .href(linkScheme.toGallery())
                                                .method(HttpMethod.GET)
                                                .relation("listAlbums")
                                                .build())
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
                                .link(
                                        LinkRepr
                                                .create()
                                                .href(linkScheme.toAlbum(album.getId()))
                                                .method(HttpMethod.POST)
                                                .relation("uploadPhoto")
                                                .build()
                                )
                                .build()
                )
                .numberOfPhotos(searchResult.getTotal())
                .photo(photoShortReprs)
                .offset(searchQuery.getOffset())
                .pageSize(searchQuery.getPageSize())
                .tag(searchQuery.getTags())
                .title(album.getTitle())
                .build();
    }
}