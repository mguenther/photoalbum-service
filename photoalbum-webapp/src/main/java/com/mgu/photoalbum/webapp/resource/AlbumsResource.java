package com.mgu.photoalbum.webapp.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.mgu.photoalbum.service.AlbumSearchRequest;
import com.mgu.photoalbum.service.AlbumSearchResult;
import com.mgu.photoalbum.webapp.converter.AlbumShortReprConverter;
import com.mgu.photoalbum.webapp.converter.GalleryReprConverter;
import com.mgu.photoalbum.webapp.representation.CreateAlbumRepr;
import com.mgu.photoalbum.webapp.representation.GalleryRepr;
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

    private final GalleryReprConverter galleryConverter;

    @Inject
    public AlbumsResource(
            final AlbumQueryService queryService,
            final AlbumCommandService commandService,
            final Authorization authorization) {
        this.queryService = queryService;
        this.commandService = commandService;
        this.authorization = authorization;
        this.linkScheme = new LinkScheme();
        this.galleryConverter = new GalleryReprConverter(linkScheme, new AlbumShortReprConverter(linkScheme));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Response createAlbum(
            @NotNull CreateAlbumRepr createAlbumRepr,
            @Auth Principal principal) {
        final String albumId = commandService.createAlbum(principal.getUserId(), createAlbumRepr.getAlbumName());
        return Response.created(linkScheme.toAlbum(albumId)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Response listAlbums(@Auth Principal principal) {

        final AlbumSearchRequest searchRequest = AlbumSearchRequest
                .create()
                .createdBy(principal.getUserId())
                .build();
        final AlbumSearchResult searchResult = queryService.search(searchRequest);

        final GalleryRepr galleryRepr = galleryConverter.convert(searchResult);
        return Response.ok(galleryRepr).build();
    }

    @OPTIONS
    @Timed
    public Response preflight() {
        return Response
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET,POST")
                .header("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization")
                .encoding("UTF-8")
                .allow("GET", "POST")
                .build();
    }
}