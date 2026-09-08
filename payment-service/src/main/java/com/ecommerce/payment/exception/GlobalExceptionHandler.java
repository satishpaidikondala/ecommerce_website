package com.ecommerce.payment.exception;

import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,String>> handleNotFound(IllegalArgumentException ex) {
        String msg = ex.getMessage()==null?"":ex.getMessage();
        if (msg.toLowerCase().contains("already exists") || msg.toLowerCase().contains("already")) return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", msg));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", msg));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField()+": "+f.getDefaultMessage()).collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", errors));
    }
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String,String>> handleData(DataAccessException ex){ log.warn("DataAccess: {}", ex.getMessage()); return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","Data access error")); }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String,String>> handleNotReadable(HttpMessageNotReadableException ex){ return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Malformed request")); }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String,String>> handleDenied(AccessDeniedException ex){ return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error","Access denied")); }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,String>> handleOther(Exception ex){ log.error("Unhandled", ex); return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error","Internal error")); }
}
