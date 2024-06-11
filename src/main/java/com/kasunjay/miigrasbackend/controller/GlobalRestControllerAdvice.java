package com.kasunjay.miigrasbackend.controller;

import com.kasunjay.miigrasbackend.common.enums.Success;
import com.kasunjay.miigrasbackend.common.exception.MainServiceException;
import com.kasunjay.miigrasbackend.common.exception.UserException;
import com.kasunjay.miigrasbackend.model.StandardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalRestControllerAdvice  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {UserException.class})
    public ResponseEntity<StandardResponse> userException(UserException exception) {
        log.error("UserException:: " + exception.getMessage());
        return new ResponseEntity<>(new StandardResponse(HttpStatus.INTERNAL_SERVER_ERROR, Success.FAILURE, exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {MainServiceException.class})
    public ResponseEntity<StandardResponse> mainServiceException(MainServiceException exception) {
        log.error("MainServiceException:: " + exception.getMessage());
        return new ResponseEntity<>(new StandardResponse(HttpStatus.INTERNAL_SERVER_ERROR, Success.FAILURE, exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
