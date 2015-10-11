package com.mgu.photoalbum.webapp.resource;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.mgu.photoalbum.domain.Album;
import com.mgu.photoalbum.security.Authorization;
import com.mgu.photoalbum.security.Principal;
import com.mgu.photoalbum.security.UserIsNotAuthorizedException;
import com.mgu.photoalbum.service.AlbumCommandService;
import com.mgu.photoalbum.service.AlbumQueryService;
import com.mgu.photoalbum.service.PhotoCommandService;
import com.mgu.photoalbum.service.PhotoQueryService;
import com.mgu.photoalbum.service.PhotoSearchRequest;
import com.mgu.photoalbum.service.PhotoSearchResult;
import com.mgu.photoalbum.webapp.converter.AlbumReprConverter;
import com.mgu.photoalbum.webapp.representation.AlbumRepr;
import io.dropwizard.auth.Auth;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Path("/albums/{albumId}")
public class AlbumResource {

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
            @QueryParam("tags") Optional<String> optionalTags) {

        final Album album = albumQueryService.albumById(albumId);

        if (!authorization.isAuthorized(principal, album)) {
            throw new UserIsNotAuthorizedException(principal);
        }

        final PhotoSearchRequest query = PhotoSearchRequest
                .create()
                .albumId(albumId)
                .offset(optionalOffset)
                .pageSize(optionalPageSize)
                .tags(parseTags(optionalTags))
                .build();
        final PhotoSearchResult result = photoQueryService.search(query);
        final AlbumRepr albumRepr = albumConverter.convert(album, result);

        return Response.ok(albumRepr).build();
    }

    private Optional<List<String>> parseTags(Optional<String> optionalTags) {
        return optionalTags.transform(i -> Arrays.asList(i.split(",")).stream().filter(s -> !s.isEmpty()).collect(Collectors.toList()));
    }

    @POST
    @Consumes("multipart/form-data")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Response upload(
            @Auth Principal principal,
            @PathParam("albumId") String albumId,
            @FormDataParam("image") InputStream fileInputStream,
            @FormDataParam("image") FormDataContentDisposition contentDisposition) {

        if (fileInputStream == null || contentDisposition == null) {
            return Response.status(422).build(); // throw exception!
        }

        final Album album = albumQueryService.albumById(albumId);
        if (!authorization.isAuthorized(principal, album)) {
            throw new UserIsNotAuthorizedException(principal);
        }

        final String photoId = photoCommandService.uploadPhoto(
                principal.getUserId(),
                albumId,
                contentDisposition.getFileName(),
                fileInputStream);

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