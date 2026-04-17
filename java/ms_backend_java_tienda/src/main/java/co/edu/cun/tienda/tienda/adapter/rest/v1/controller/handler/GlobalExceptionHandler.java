package co.edu.cun.tienda.tienda.adapter.rest.v1.controller.handler;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.edu.cun.tienda.tienda.domain.exception.ExternalServiceException;
import co.edu.cun.tienda.tienda.domain.exception.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final ZoneId BOGOTA_ZONE_ID = ZoneId.of("America/Bogota");

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProductNotFoundException(
            ProductNotFoundException ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", OffsetDateTime.now(BOGOTA_ZONE_ID),
                "status", 404,
                "error", "Not Found",
                "message", ex.getMessage(),
                "path", request.getRequestURI()
        ));
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<Map<String, Object>> handleExternalServiceException(
            ExternalServiceException ex, HttpServletRequest request) {

        HttpStatus status;
        int code = ex.getStatusCode().value();
        if (code == 404) {
            status = HttpStatus.NOT_FOUND;
        } else if (code == 503) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
        } else {
            status = HttpStatus.BAD_GATEWAY;
        }

        return ResponseEntity.status(status).body(Map.of(
                "timestamp", OffsetDateTime.now(BOGOTA_ZONE_ID),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", ex.getMessage(),
                "path", request.getRequestURI()
        ));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "timestamp", OffsetDateTime.now(BOGOTA_ZONE_ID),
                "status", 500,
                "error", "Internal Server Error",
                "message", ex.getMessage() != null ? ex.getMessage() : "Unexpected error",
                "path", request.getRequestURI()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "timestamp", OffsetDateTime.now(BOGOTA_ZONE_ID),
                "status", 500,
                "error", "Internal Server Error",
                "message", ex.getMessage() != null ? ex.getMessage() : "Unexpected error",
                "path", request.getRequestURI()
        ));
    }
}
