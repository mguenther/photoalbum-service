package com.mgu.photoalbum.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.mgu.photoalbum.converter.AlbumShortReprConverter;
import com.mgu.photoalbum.converter.GalleryConverter;
import com.mgu.photoalbum.representation.CreateAlbumRepr;
import com.mgu.photoalbum.representation.GalleryRepr;
import com.mgu.photoalbum.security.Authorization;
import com.mgu.photoalbum.security.Principal;
import com.mgu.photoalbum.service.AlbumCommandService;
import com.mgu.photoalbum.service.AlbumQueryService;
import io.dropwizard.auth.Auth;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/albums")
public class AlbumsResource {

    private final AlbumQueryService queryService;

    private final AlbumCommandService commandService;

    private final Authorization authorization;

    private final LinkScheme linkScheme;

    private final GalleryConverter galleryConverter;

    @Inject
    public AlbumsResource(
            final AlbumQueryService queryService,
            final AlbumCommandService commandService,
            final Authorization authorization) {
        this.queryService = queryService;
        this.commandService = commandService;
        this.authorization = authorization;
        this.linkScheme = new LinkScheme();
        this.galleryConverter = new GalleryConverter(linkScheme, new AlbumShortReprConverter(linkScheme));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Response createAlbum(
            @NotNull CreateAlbumRepr createAlbumRepr,
            @Auth Principal principal) {

        /*if (createAlbumRepr == null) {
            return Response.status(422).build();
        }*/

        final String albumId = commandService.createAlbum(principal.getUserId(), createAlbumRepr.getAlbumName());
        return Response.created(linkScheme.toAlbum(albumId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Response listAlbums(
            @Auth Principal principal) {

        final GalleryRepr galleryRepr = galleryConverter.convert(queryService.albumsByOwner(principal.getUserId()));
        return Response.ok(galleryRepr).build();
    }
}