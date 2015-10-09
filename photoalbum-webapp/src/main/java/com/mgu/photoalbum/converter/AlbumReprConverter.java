package com.mgu.photoalbum.converter;

import com.google.inject.Inject;
import com.mgu.photoalbum.domain.Album;
import com.mgu.photoalbum.domain.Photo;
import com.mgu.photoalbum.representation.AlbumRepr;
import com.mgu.photoalbum.representation.LinkRepr;
import com.mgu.photoalbum.representation.MetaRepr;
import com.mgu.photoalbum.representation.PhotoShortRepr;
import com.mgu.photoalbum.resource.LinkScheme;

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

    public AlbumRepr convert(final Album album, final List<Photo> photos, final int offset, final int pageSize, final List<String> tags) {

        final List<PhotoShortRepr> photoShortReprs = photos
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
                .numberOfPhotos(album.getContainingPhotos().size())
                .photo(photoShortReprs)
                .offset(offset)
                .pageSize(pageSize)
                .tag(tags)
                .title(album.getTitle())
                .build();
    }
}