package com.legacy.inventory.exception;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * OUTDATED PATTERNS:
 * - Returns raw HashMap instead of typed error response
 * - Uses java.util.Date
 * - Uses javax.servlet
 * - Log4j 1.x
 * - Catches generic Exception
 * - Exposes stack trace details (security risk)
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        logger.error("RuntimeException: " + ex.getMessage(), ex);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", new Date());
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("error", "Internal Server Error");
        // SECURITY RISK: Exposing exception message to client
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("path", request.getRequestURI());
        // SECURITY RISK: Exposing stack trace
        errorResponse.put("trace", ex.getStackTrace());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex, HttpServletRequest request) {
        logger.error("Unhandled exception: " + ex.getMessage(), ex);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", new Date());
        errorResponse.put("status", 500);
        errorResponse.put("error", ex.getClass().getSimpleName());
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("path", request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
