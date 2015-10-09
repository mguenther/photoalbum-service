package com.mgu.photoalbum.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.mgu.photoalbum.converter.PhotoMetadataReprConverter;
import com.mgu.photoalbum.domain.Photo;
import com.mgu.photoalbum.representation.UpdateMetadataRepr;
import com.mgu.photoalbum.security.Authorization;
import com.mgu.photoalbum.security.Principal;
import com.mgu.photoalbum.security.UserIsNotAuthorizedException;
import com.mgu.photoalbum.service.PhotoCommandService;
import com.mgu.photoalbum.service.PhotoQueryService;
import io.dropwizard.auth.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class MetadataResource {

    private final PhotoCommandService commandService;

    private final PhotoQueryService queryService;

    private final Authorization authorization;

    private final PhotoMetadataReprConverter converter;

    @Inject
    public MetadataResource(
            final PhotoCommandService commandService,
            final PhotoQueryService queryService,
            final Authorization authorization,
            final PhotoMetadataReprConverter converter) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.authorization = authorization;
        this.converter = converter;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Response viewMetadata(
            @Auth Principal principal,
            @PathParam("albumId") String albumId,
            @PathParam("photoId") String photoId) {

        final Photo photo = queryService.photoById(photoId);

        if (!authorization.isAuthorized(principal, photo)) {
            throw new UserIsNotAuthorizedException(principal);
        }

        return Response.ok(converter.convert(photo)).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Response updateMetadata(
            @Auth Principal principal,
            @PathParam("albumId") String albumId,
            @PathParam("photoId") String photoId,
            UpdateMetadataRepr updateMetadataRepr) {

        final Photo photo = queryService.photoById(photoId);

        if (!authorization.isAuthorized(principal, photo)) {
            throw new UserIsNotAuthorizedException(principal);
        }

        commandService.updateMetadata(photoId, updateMetadataRepr.getDescription(), updateMetadataRepr.getTags());
        return Response.noContent().build();
    }
}