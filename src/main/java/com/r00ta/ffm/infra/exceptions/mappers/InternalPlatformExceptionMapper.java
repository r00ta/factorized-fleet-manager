package com.r00ta.ffm.infra.exceptions.mappers;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.r00ta.ffm.api.models.responses.ErrorResponse;
import com.r00ta.ffm.infra.exceptions.DinosaurError;
import com.r00ta.ffm.infra.exceptions.DinosaurErrorService;
import com.r00ta.ffm.infra.exceptions.definitions.platform.InternalPlatformException;
import io.quarkus.runtime.Quarkus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InternalPlatformExceptionMapper implements ExceptionMapper<InternalPlatformException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternalPlatformExceptionMapper.class);
    private static final String MESSAGE_TEMPLATE = "There was an internal exception that is not fixable from the user. Please " +
            "open a bug with all the information you have, including the name of the exception %s and the message %s";

    private DinosaurError internalPlatformExceptionBridgeError;

    @Inject
    DinosaurErrorService bridgeErrorService;

    @PostConstruct
    void init() {
        Optional<DinosaurError> error = bridgeErrorService.getError(InternalPlatformException.class);
        if (error.isPresent()) {
            internalPlatformExceptionBridgeError = error.get();
        } else {
            LOGGER.error("InternalPlatformException error is not defined in the ErrorsService.");
            Quarkus.asyncExit(1);
        }
    }

    @Override
    public Response toResponse(InternalPlatformException e) {
        LOGGER.warn("Internal Platform exception", e);

        // InternalPlatformException is always returned.
        Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        ErrorResponse errorResponse = ErrorResponse.from(internalPlatformExceptionBridgeError);
        errorResponse.setReason(String.format(MESSAGE_TEMPLATE, e.getClass().getName(), e.getMessage()));
        builder.entity(errorResponse);
        return builder.build();
    }
}
