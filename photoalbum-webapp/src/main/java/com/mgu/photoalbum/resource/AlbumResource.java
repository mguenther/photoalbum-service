package com.mgu.photoalbum.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.mgu.photoalbum.converter.AlbumReprConverter;
import com.mgu.photoalbum.domain.Album;
import com.mgu.photoalbum.domain.Photo;
import com.mgu.photoalbum.representation.AlbumRepr;
import com.mgu.photoalbum.representation.UploadPhotoRepr;
import com.mgu.photoalbum.security.Authorization;
import com.mgu.photoalbum.security.Principal;
import com.mgu.photoalbum.security.UserIsNotAuthorizedException;
import com.mgu.photoalbum.service.AlbumCommandService;
import com.mgu.photoalbum.service.AlbumQueryService;
import com.mgu.photoalbum.service.PhotoCommandService;
import com.mgu.photoalbum.service.PhotoQueryService;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Path("/albums/{albumId}")
public class AlbumResource {

    private static final int MAX_PAGE_SIZE = 100;

    private static final int DEFAULT_OFFSET = 0;

    private static final int DEFAULT_PAGE_SIZE = 10;

    private static final List<String> DEFAULT_FILTER_BY_TAGS = new ArrayList<>();

    private final AlbumQueryService albumQueryService;

    private final AlbumCommandService albumCommandService;

    private final PhotoCommandService photoCommandService;

    private final PhotoQueryService photoQueryService;

    private final Authorization authorization;

    private final AlbumReprConverter albumConverter;

    private final LinkScheme linkScheme;

    @Inject
    public AlbumResource(
            final AlbumCommandService albumCommandService,
            final AlbumQueryService albumQueryService,
            final PhotoQueryService photoQueryService,
            final PhotoCommandService photoCommandService,
            final Authorization authorization,
            final AlbumReprConverter albumConverter) {
        this.albumCommandService = albumCommandService;
        this.albumQueryService = albumQueryService;
        this.photoQueryService = photoQueryService;
        this.photoCommandService = photoCommandService;
        this.authorization = authorization;
        this.albumConverter = albumConverter;
        this.linkScheme = new LinkScheme();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Response listAlbum(
            @Auth Principal principal,
            @PathParam("albumId") String albumId,
            @QueryParam("offset") Optional<Integer> optionalOffset,
            @QueryParam("pageSize") Optional<Integer> optionalPageSize,
            @QueryParam("tags") Optional<String> optionalTags)  {

        final Album album = albumQueryService.albumById(albumId);

        if (!authorization.isAuthorized(principal, album)) {
            throw new UserIsNotAuthorizedException(principal);
        }

        final int offset = optionalOffset
                .transform(wrappedOffset -> max(DEFAULT_OFFSET, wrappedOffset))
                .or(DEFAULT_OFFSET);
        final int pageSize = optionalPageSize
                .transform(wrappedPageSize -> min(wrappedPageSize, MAX_PAGE_SIZE))
                .or(DEFAULT_PAGE_SIZE);
        final List<String> filterByTags = parseTags(optionalTags).or(DEFAULT_FILTER_BY_TAGS);


        final List<Photo> photos = photoQueryService.search(albumId, filterByTags, offset, pageSize);
        final AlbumRepr albumRepr = albumConverter.convert(album, photos, offset, pageSize, filterByTags);

        return Response.ok(albumRepr).build();
    }

    private Optional<List<String>> parseTags(Optional<String> optionalTags) {
        return optionalTags.transform(i -> Arrays.asList(i.split(",")).stream().filter(s -> !s.isEmpty()).collect(Collectors.toList()));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Response uploadPhoto(
            @Auth Principal principal,
            @PathParam("albumId") String albumId,
            UploadPhotoRepr uploadPhotoRepr) {

        final Album album = albumQueryService.albumById(albumId);

        if (!authorization.isAuthorized(principal, album)) {
            throw new UserIsNotAuthorizedException(principal);
        }

        final String photoId = photoCommandService.uploadPhoto(
                principal.getUserId(),
                albumId,
                uploadPhotoRepr.getOriginalFilename(),
                uploadPhotoRepr.getBase64EncodedImage());

        return Response.created(linkScheme.toPhoto(albumId, photoId)).build();
    }

    @DELETE
    @Timed
    public Response deleteAlbum(
            @Auth Principal principal,
            @PathParam("albumId") String albumId) {

        final Album album = albumQueryService.albumById(albumId);

        if (!authorization.isAuthorized(principal, album)) {
            throw new UserIsNotAuthorizedException(principal);
        }

        albumCommandService.deleteAlbum(albumId);
        return Response.noContent().build();
    }

    @OPTIONS
    @Timed
    public Response preflight() {
        return Response
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET,POST,DELETE")
                .header("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization")
                .encoding("UTF-8")
                .allow("GET", "POST", "DELETE")
                .build();
    }
}