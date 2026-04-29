package com.example.demo.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.demo.response.Apiresponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Apiresponse<Object>> handleResourceNotFound(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Apiresponse<>(e.getMessage(), 404, null));
    }
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Apiresponse<Object>> handleBusinessException(BusinessException e) {
        HttpStatus status = "Email already registered".equalsIgnoreCase(e.getMessage())
                ? HttpStatus.CONFLICT
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status)
                .body(new Apiresponse<>(e.getMessage(), status.value(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Apiresponse<Object>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Apiresponse<>("Validation failed", 400, errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Apiresponse<Object>> handleConstraintViolation(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .findFirst()
                .map(violation -> violation.getMessage())
                .orElse("Validation failed");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Apiresponse<>(message, 400, null));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Apiresponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new Apiresponse<>("Profile data conflicts with an existing record", 409, null));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Apiresponse<Object>> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Apiresponse<>("Something went wrong", 500, null));
    }
}
