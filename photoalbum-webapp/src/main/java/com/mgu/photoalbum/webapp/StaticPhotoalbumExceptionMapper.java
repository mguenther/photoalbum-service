package com.mgu.photoalbum.webapp;

import com.mgu.photoalbum.adapter.fileio.UnableToCopyFileException;
import com.mgu.photoalbum.adapter.fileio.UnableToDecodeFromBase64Exception;
import com.mgu.photoalbum.adapter.fileio.UnableToReadFromFilesystemException;
import com.mgu.photoalbum.adapter.fileio.UnableToWriteToFilesystemException;
import com.mgu.photoalbum.exception.PhotoalbumException;
import com.mgu.photoalbum.security.UserIsNotAuthorizedException;
import com.mgu.photoalbum.service.AlbumDoesNotExistException;
import com.mgu.photoalbum.service.ImageDoesNotExistException;
import com.mgu.photoalbum.service.PhotoDoesNotExistException;
import com.mgu.photoalbum.service.UnableToUpdateAlbumException;
import com.mgu.photoalbum.service.UnableToUpdateMetadataException;
import com.mgu.photoalbum.service.scaler.UnableToScaleImageException;
import com.mgu.photoalbum.webapp.representation.ErrorRepr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class StaticPhotoalbumExceptionMapper implements ExceptionMapper<PhotoalbumException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StaticPhotoalbumExceptionMapper.class);

    private static final Map<Class<? extends PhotoalbumException>, Integer> EXCEPTION_TO_HTTP_STATUS = new HashMap<>();

    static {
        EXCEPTION_TO_HTTP_STATUS.put(UnableToReadFromFilesystemException.class, Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        EXCEPTION_TO_HTTP_STATUS.put(UnableToWriteToFilesystemException.class, Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        EXCEPTION_TO_HTTP_STATUS.put(UnableToCopyFileException.class, Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        EXCEPTION_TO_HTTP_STATUS.put(UnableToDecodeFromBase64Exception.class, Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        EXCEPTION_TO_HTTP_STATUS.put(UserIsNotAuthorizedException.class, Response.Status.FORBIDDEN.getStatusCode());

        EXCEPTION_TO_HTTP_STATUS.put(AlbumDoesNotExistException.class, Response.Status.NOT_FOUND.getStatusCode());
        EXCEPTION_TO_HTTP_STATUS.put(UnableToUpdateAlbumException.class, Response.Status.CONFLICT.getStatusCode());

        EXCEPTION_TO_HTTP_STATUS.put(PhotoDoesNotExistException.class, Response.Status.NOT_FOUND.getStatusCode());
        EXCEPTION_TO_HTTP_STATUS.put(UnableToScaleImageException.class, Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        EXCEPTION_TO_HTTP_STATUS.put(UnableToUpdateMetadataException.class, Response.Status.CONFLICT.getStatusCode());
        EXCEPTION_TO_HTTP_STATUS.put(ImageDoesNotExistException.class, Response.Status.NOT_FOUND.getStatusCode());
    }

    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(final PhotoalbumException exception) {

        LOGGER.warn(formatErrorMessage(exception));

        final int httpStatus = EXCEPTION_TO_HTTP_STATUS.get(exception.getClass());
        return Response
                .status(httpStatus)
                .entity(error(exception, httpStatus))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private String formatErrorMessage(final PhotoalbumException exception) {

        if (request == null) {
            final StringBuilder sb = new StringBuilder();
            sb
                    .append("Unable to answer request due to caught Exception with error: ")
                    .append(exception.getMessage())
                    .append(" (")
                    .append(exception.getErrorCode())
                    .append(").");
            return sb.toString();
        }

        final StringBuilder sb = new StringBuilder();
        sb
                .append("Unable to answer request to ")
                .append(request.getRequestURI())
                .append(" from ")
                .append(request.getRemoteAddr())
                .append(":")
                .append(request.getRemotePort())
                .append(" due to caught Exception with error: ")
                .append(exception.getMessage())
                .append(" (")
                .append(exception.getErrorCode())
                .append(").");
        return sb.toString();
    }

    private ErrorRepr error(final PhotoalbumException exception, final int httpStatus) {
        final ErrorRepr errorRepr = new ErrorRepr();
        errorRepr.setErrorCode(exception.getErrorCode());
        errorRepr.setTitle(exception.getMessage());
        errorRepr.setHttpStatus(httpStatus);
        return errorRepr;
    }
}