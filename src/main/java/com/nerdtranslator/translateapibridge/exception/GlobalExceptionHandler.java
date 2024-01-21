package com.nerdtranslator.translateapibridge.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * If you created your own exceptions, then
     * add your special exception handlers for it down below.
     **/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        String errorMessage = "An error occurred: " + ex.getMessage();
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
    }

    @ExceptionHandler(GoogleApiResponseException.class)
    public ResponseEntity<Object> handleGoogleApiResponseException(GoogleApiResponseException ex) {
        String errorMessage = "Bad gateway, an error occurred in " + ex.getMessage();
        return buildErrorResponse(HttpStatus.BAD_GATEWAY, errorMessage);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        String errorMessage = "Bad request: " + ex.getMessage();
        return buildErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    private static ResponseEntity<Object> buildErrorResponse(HttpStatus httpStatus, String message) {
        ErrorResponse response = new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), message);
        return ResponseEntity.status(httpStatus.value()).body(response);
    }

    @Getter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class ErrorResponse {
        private int status;
        private String error;
        private String message;
    }

    @Getter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class ErrorArrayResponse {
        private int status;
        private String error;
        private String[] message;
    }
}
