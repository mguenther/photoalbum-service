package com.mgu.photoalbum.webapp;

import com.mgu.photoalbum.webapp.representation.ErrorRepr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class StaticThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StaticThrowableExceptionMapper.class);

    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(final Throwable throwable) {
        LOGGER.warn(formatErrorMessage(throwable));
        return Response.status(500).entity(error(throwable)).type(MediaType.APPLICATION_JSON).build();
    }

    private String formatErrorMessage(final Throwable throwable) {

        if (request == null) {
            final StringBuilder sb = new StringBuilder();

            sb
                    .append("Unable to answer request due to caught Exception with error: ")
                    .append(throwable.getMessage())
                    .append(" (GFFF).");
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
                .append(throwable.getMessage())
                .append(" (GFFF).");
        return sb.toString();
    }

    private ErrorRepr error(final Throwable t) {
        final ErrorRepr errorRepr = new ErrorRepr();
        errorRepr.setErrorCode("GFFF");
        errorRepr.setTitle(t.getMessage());
        errorRepr.setHttpStatus(500);
        return errorRepr;
    }
}