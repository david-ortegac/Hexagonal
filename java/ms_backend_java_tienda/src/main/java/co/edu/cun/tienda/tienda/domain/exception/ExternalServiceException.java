package co.edu.cun.tienda.tienda.domain.exception;

import org.springframework.http.HttpStatusCode;

public class ExternalServiceException extends RuntimeException {

    private final HttpStatusCode statusCode;

    public ExternalServiceException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}
