package com.mgu.photoalbum.converter;

import com.google.inject.Inject;
import com.mgu.photoalbum.domain.Album;
import com.mgu.photoalbum.representation.AlbumShortRepr;
import com.mgu.photoalbum.representation.GalleryRepr;
import com.mgu.photoalbum.representation.LinkRepr;
import com.mgu.photoalbum.representation.MetaRepr;
import com.mgu.photoalbum.resource.LinkScheme;

import javax.ws.rs.HttpMethod;
import java.util.List;
import java.util.stream.Collectors;

public class GalleryConverter implements Converter<List<Album>, GalleryRepr> {

    private final LinkScheme linkScheme;

    private final AlbumShortReprConverter albumConverter;

    @Inject
    public GalleryConverter(final LinkScheme linkScheme, final AlbumShortReprConverter albumConverter) {
        this.linkScheme = linkScheme;
        this.albumConverter = albumConverter;
    }

    @Override
    public GalleryRepr convert(final List<Album> albums) {

        final List<AlbumShortRepr> albumShortReprs = albums
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
