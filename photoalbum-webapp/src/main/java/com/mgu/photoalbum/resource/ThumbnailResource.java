package com.mgu.photoalbum.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.mgu.photoalbum.domain.Photo;
import com.mgu.photoalbum.security.Authorization;
import com.mgu.photoalbum.security.Principal;
import com.mgu.photoalbum.security.UserIsNotAuthorizedException;
import com.mgu.photoalbum.service.PhotoQueryService;
import io.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/albums/{albumId}/{photoId}/thumbnail")
public class ThumbnailResource {

    private final PhotoQueryService queryService;

    private final Authorization authorization;

    @Inject
    public ThumbnailResource(final PhotoQueryService photoQueryService, final Authorization authorization) {
        this.queryService = photoQueryService;
        this.authorization = authorization;
    }

    @GET
    @Produces("image/jpeg")
    @Timed
    public Response viewThumbnail(
            @Auth Principal principal,
            @PathParam("albumId") String albumId,
            @PathParam("photoId") String photoId) {

        final Photo photo = queryService.photoById(photoId);

        if (!authorization.isAuthorized(principal, photo)) {
            throw new UserIsNotAuthorizedException(principal);
        }

        final byte[] thumbnailImage = queryService.thumbnailById(photoId);
        return Response.ok(thumbnailImage, "image/jpeg").header("Content-Length", String.valueOf(thumbnailImage.length)).build();
    }

    @OPTIONS
    @Timed
    public Response preflight() {
        return Response
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .header("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization")
                .encoding("UTF-8")
                .allow("GET")
                .build();
    }
}