package org.example.controllers.errors;

import java.util.UUID;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class ThrowableMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable e) {
        String errorId = UUID.randomUUID().toString();
        log.error("errorId[{}]", errorId, e);
        ErrorResponse.ErrorMessage errorMessage = new ErrorResponse.ErrorMessage(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(errorId, errorMessage);
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .build();
    }

}
