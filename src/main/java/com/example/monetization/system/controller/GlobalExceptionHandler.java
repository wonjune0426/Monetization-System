package com.example.monetization.system.controller;


import com.example.monetization.system.dto.response.ResponseEntityDto;
import com.example.monetization.system.exception.AdNotFoundException;
import com.example.monetization.system.exception.JwtValidationException;
import com.example.monetization.system.exception.VideoDeleteException;
import com.example.monetization.system.exception.VideoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VideoNotFoundException.class)
    public ResponseEntity<ResponseEntityDto<Void>> handleVideoNotFoundException(VideoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseEntityDto<>(ex.getMessage()));
    }

    @ExceptionHandler(VideoDeleteException.class)
    public ResponseEntity<ResponseEntityDto<Void>> handleVideoDeleteException(VideoDeleteException ex){
        return ResponseEntity.status(HttpStatus.GONE).body(new ResponseEntityDto<>(ex.getMessage()));
    }

    @ExceptionHandler(AdNotFoundException.class)
    public ResponseEntity<ResponseEntityDto<Void>> handleAdNotFoundException(AdNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseEntityDto<>(ex.getMessage()));
    }

    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<ResponseEntityDto<Void>> handleJwtValidationException(JwtValidationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseEntityDto<>(ex.getMessage()));
    }
}
