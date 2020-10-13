package com.aytekin.manytomany.controller;

import com.aytekin.manytomany.exception.ExceptionResponse;
import com.aytekin.manytomany.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * Exception Resolver.
 * Unhandled exceptions in Controller will be formatted here.
 *
 * @author dnardelli
 */
@ControllerAdvice
@RestController
public class ExceptionResolver {

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleNotFoundException(ResourceNotFoundException ex, WebRequest req) {
        String path = ((ServletWebRequest) req).getRequest().getRequestURI();
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(), ex.getMessage(), path, HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<ExceptionResponse> handleBadRequestException(HttpMessageNotReadableException ex, WebRequest req) {
        String path = ((ServletWebRequest) req).getRequest().getRequestURI();
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(), "Invalid request", path, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ExceptionResponse> handleInvalidCredentialsException(IllegalArgumentException ex, WebRequest req) {
        String path = ((ServletWebRequest) req).getRequest().getRequestURI();
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(), "Illegal argument", path, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
