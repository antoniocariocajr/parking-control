package com.bill.parking_control.config.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import com.bill.parking_control.dtos.exception.StandardError;
import com.bill.parking_control.dtos.exception.ValidationError;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<StandardError> responseStatus(ResponseStatusException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                e.getReason(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError err = new ValidationError(
                Instant.now(),
                status.value(),
                "Validation Error",
                "Invalid input data",
                request.getRequestURI());

        for (FieldError f : e.getBindingResult().getFieldErrors()) {
            err.addError(f.getField(), f.getDefaultMessage());
        }

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> database(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Internal Server Error",
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}
