package com.mgu.photoalbum.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.mgu.photoalbum.domain.Photo;
import com.mgu.photoalbum.security.Authorization;
import com.mgu.photoalbum.security.Principal;
import com.mgu.photoalbum.security.UserIsNotAuthorizedException;
import com.mgu.photoalbum.service.PhotoCommandService;
import com.mgu.photoalbum.service.PhotoQueryService;
import io.dropwizard.auth.Auth;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/albums/{albumId}/{photoId}")
public class PhotoResource {

    private final PhotoCommandService commandService;

    private final PhotoQueryService queryService;

    private final Authorization authorization;

    public PhotoResource(final PhotoCommandService commandService, final PhotoQueryService queryService, final Authorization authorization) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.authorization = authorization;
    }

    @GET
    @Produces("image/jpeg")
    @Timed
    public Response viewPhoto(
            @Auth Principal principal,
            @PathParam("albumId") String albumId,
            @PathParam("photoId") String photoId,
            @QueryParam("download") Optional<Boolean> optionalDownload) {

        final Photo photo = queryService.photoById(photoId);

        if (!authorization.isAuthorized(principal, photo)) {
            throw new UserIsNotAuthorizedException(principal);
        }

        final boolean downloadRequested = optionalDownload.or(false);
        final byte[] originalImage = queryService.originalById(photoId);

        if (downloadRequested) {
            return Response
                    .ok(originalImage, "image/jpeg")
                    .header("Content-Length", String.valueOf(originalImage.length))
                    .header("Content-Disposition", "attachment; filename=\"" + photo.getOriginalFilename() + "\"")
                    .build();
        } else {
            return Response
                    .ok(originalImage, "image/jpeg")
                    .header("Content-Length", String.valueOf(originalImage.length))
                    .build();
        }
    }

    @DELETE
    @Timed
    public Response deletePhoto(
            @Auth Principal principal,
            @PathParam("albumId") String albumId,
            @PathParam("photoId") String photoId) {

        final Photo photo = queryService.photoById(photoId);

        if (!authorization.isAuthorized(principal, photo)) {
            throw new UserIsNotAuthorizedException(principal);
        }

        commandService.deletePhoto(photoId);
        return Response.noContent().build();
    }
}