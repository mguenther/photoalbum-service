package com.mgu.photoalbum.webapp;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mgu.photoalbum.webapp.representation.ErrorRepr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Arrays;

public class CustomJsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomJsonProcessingExceptionMapper.class);

    private final boolean showDetails;

    public CustomJsonProcessingExceptionMapper() {
        this(false);
    }

    public CustomJsonProcessingExceptionMapper(final boolean showDetails) {
        this.showDetails = showDetails;
    }

    @Override
    public Response toResponse(final JsonProcessingException exception) {

        final String message = exception.getOriginalMessage();

        if (isUnableToGenerateJson(exception)) {
            LOGGER.warn("Error while generating JSON.", exception);
            return unableToGenerateJson();
        } else if (isUnableToFindSuitableConstructor(message)) {
            LOGGER.warn("Unable to deserializie the specific type.", exception);
            return unableToFindSuitableConstructor();
        } else {
            LOGGER.warn("Unable to process given JSON.", exception);
            return unableToProcessJson(message);
        }
    }

    private boolean isUnableToGenerateJson(final JsonProcessingException exception) {
        return (exception instanceof JsonGenerationException);
    }

    private boolean isUnableToFindSuitableConstructor(final String message) {
        return message.startsWith("No suitable constructor found");
    }

    private Response unableToGenerateJson() {
        final ErrorRepr errorRepr = new ErrorRepr();
        errorRepr.setErrorCode("G010");
        errorRepr.setTitle("Unable to generate JSON.");
        errorRepr.setHttpStatus(500);
        return Response
                .status(errorRepr.getHttpStatus())
                .entity(errorRepr)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private Response unableToFindSuitableConstructor() {
        final ErrorRepr errorRepr = new ErrorRepr();
        errorRepr.setErrorCode("G011");
        errorRepr.setTitle("No suitable constructor found to deserialize the given data.");
        errorRepr.setHttpStatus(500);
        return Response
                .status(errorRepr.getHttpStatus())
                .entity(errorRepr)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private Response unableToProcessJson(final String optionalMessage) {
        final ErrorRepr errorRepr = new ErrorRepr();
        errorRepr.setErrorCode("G012");
        errorRepr.setTitle("Unable to process JSON.");
        if (this.showDetails) {
            errorRepr.setDetails(Arrays.asList(optionalMessage));
        }
        errorRepr.setHttpStatus(400);
        return Response
                .status(errorRepr.getHttpStatus())
                .entity(errorRepr)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}