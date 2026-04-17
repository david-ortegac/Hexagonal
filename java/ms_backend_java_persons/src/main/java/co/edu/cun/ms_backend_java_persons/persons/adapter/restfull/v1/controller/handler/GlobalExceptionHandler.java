package co.edu.cun.ms_backend_java_persons.persons.adapter.restfull.v1.controller.handler;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import co.edu.cun.ms_backend_java_persons.persons.domain.exception.PersonNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final ZoneId BOGOTA_ZONE_ID = ZoneId.of("America/Bogota");

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePersonNotFound(PersonNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<Map<String, Object>> handleBadRequest(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Request payload or parameters are invalid",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpectedError(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred while processing the request",
                request.getRequestURI()
        );
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String detail, String path) {
        Map<String, Object> errorBody = new LinkedHashMap<>();
        errorBody.put("detail", detail);
        errorBody.put("status", status.value());
        errorBody.put("path", path);
        errorBody.put("timestamp", OffsetDateTime.now(BOGOTA_ZONE_ID));
        return ResponseEntity.status(status).body(errorBody);
    }
}
