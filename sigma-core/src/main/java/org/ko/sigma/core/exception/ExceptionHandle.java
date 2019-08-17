package org.ko.sigma.core.exception;

import org.ko.sigma.core.support.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandle extends ResponseEntityExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(ValidateException.class)
    public Response validateException(ValidateException ve, WebRequest webRequest) {
        return Response.of(Response.FAILED, ve.getCode(), ve.getMessage());
    }

    @ExceptionHandler(GeneralException.class)
    public Response generalException(GeneralException ge, WebRequest request) {
        return Response.of(Response.FAILED, ge.getCode(), ge.getMessage());
    }

}
