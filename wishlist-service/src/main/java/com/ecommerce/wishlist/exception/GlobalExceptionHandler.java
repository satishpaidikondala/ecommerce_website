package com.ecommerce.wishlist.exception;

import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,String>> handle(IllegalArgumentException ex){
        String msg=ex.getMessage()==null?"":ex.getMessage();
        if(msg.toLowerCase().contains("already")) return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error",msg));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error",msg));
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String,String>> handleConflict(DataIntegrityViolationException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","Already in wishlist"));
    }
}
