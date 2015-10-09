package com.mgu.photoalbum.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mgu.photoalbum.representation.CreateAlbumRepr;
import com.mgu.photoalbum.security.Authorization;
import com.mgu.photoalbum.security.Principal;
import com.mgu.photoalbum.service.AlbumCommandService;
import com.mgu.photoalbum.service.AlbumQueryService;
import io.dropwizard.auth.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/albums")
public class AlbumsResource {

    private final AlbumQueryService queryService;

    private final AlbumCommandService commandService;

    private final Authorization authorization;

    private final PhotoalbumLinkScheme linkScheme;

    @Inject
    public AlbumsResource(final AlbumQueryService queryService, final AlbumCommandService commandService, final Authorization authorization, @Named("hostname") final String hostname) {
        this.queryService = queryService;
        this.commandService = commandService;
        this.authorization = authorization;
        this.linkScheme = new PhotoalbumLinkScheme(hostname);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Response createAlbum(
            CreateAlbumRepr createAlbumRepr,
            @Auth Principal principal) {
        if (createAlbumRepr == null) {
            return Response.status(422).build();
        }

        final String albumId = commandService.createAlbum(principal.getUserId(), createAlbumRepr.getAlbumName());
        return Response.created(linkScheme.toAlbum(albumId)).build();
    }
}