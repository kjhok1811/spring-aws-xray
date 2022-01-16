package com.example.awsxray.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class XrayControllerAdvice {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity entityNotFoundExceptionHandler() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
