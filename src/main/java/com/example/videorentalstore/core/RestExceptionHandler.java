package com.example.videorentalstore.core;

import com.example.videorentalstore.film.FilmNotFoundException;
import lombok.Data;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({FilmNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(Exception e) {
        return buildResponseEntity(RestError.of(HttpStatus.NOT_FOUND, e));
    }

    private ResponseEntity<Object> buildResponseEntity(RestError restError) {
        return ResponseEntity.status(restError.getStatus()).body(restError);
    }

    /**
     * REST error object.
     */
    @Data
    static class RestError {

        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String exception;
        private String message;
        private List<String> errors;

        private RestError(LocalDateTime timestamp,
                          HttpStatus status,
                          String error,
                          String exception,
                          String message,
                          List<String> errors) {
            this.timestamp = timestamp;
            this.status = status.value();
            this.error = error;
            this.exception = exception;
            this.message = message;
            this.errors = errors;
        }

        public static RestError of(HttpStatus status, Throwable ex) {
            return of(status, ex, ex.getLocalizedMessage());
        }

        public static RestError of(HttpStatus status, Throwable ex, String message) {
            return new RestError(
                    LocalDateTime.now(),
                    status,
                    status.getReasonPhrase(),
                    ex.getClass().getName(),
                    message,
                    null);
        }

        public void addValidationErrors(List<? extends ObjectError> bindErrors) {
            this.errors = bindErrors.stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        }
    }
}
