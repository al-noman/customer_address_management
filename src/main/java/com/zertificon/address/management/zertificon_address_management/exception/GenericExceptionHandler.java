package com.zertificon.address.management.zertificon_address_management.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Date;

@RestController
@ControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<Object> handleEntityNotFoundException(
            EntityNotFoundException exception, WebRequest request){

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
                exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestParamValueException.class)
    public final ResponseEntity<Object> handleBadRequestException(
            BadRequestParamValueException exception, WebRequest request){

        ExceptionResponse response = new ExceptionResponse(new Date(), exception.getParamName(),
                exception.getParamValue(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException exception, WebRequest request){

        ExceptionResponse response = new ExceptionResponse(
                new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(
                new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ExceptionResponse response = new ExceptionResponse(
                new Date(), "Validation failed", ex.getBindingResult().toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
