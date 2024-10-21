package com.guaguaupop.guaguaupop.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.validation.ConstraintViolation;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /*// Excepciones de producto
    @ExceptionHandler({ProductoNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFound(Exception ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorResponse);
    }

    @ExceptionHandler({ProductUnauthorizedAccessException.class})
    public ResponseEntity<ApiErrorResponse> handleNotAccess(Exception ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiErrorResponse);
    }*/

    // Excepciones de validacion
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = getErrorMessagesAsMap(e.getBindingResult().getAllErrors());

        ApiErrorResponseJSON apiErrorResponseJSON = new ApiErrorResponseJSON(HttpStatus.BAD_REQUEST, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponseJSON);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }

    private Map<String, String> getErrorMessagesAsMap(List<ObjectError> allErrors) {
        return allErrors.stream()
                .collect(Collectors.toMap(
                        error -> ((FieldError) error).getField(),
                        ObjectError::getDefaultMessage
                ));
    }

    // Excepciones de base de datos
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponseJSON> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        System.out.println(e.getErrorCode());
        Map<String, String> message = new HashMap<>();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if(e.getErrorCode() == 1062 && e.getMessage().contains("unique_email")) {
            message.put("email_duplicate", "El email ya está registrado.");
            status = HttpStatus.CONFLICT;
        } else if(e.getErrorCode() == 1062) {
            message.put("username_duplicate", "El nombre de usuario ya existe, prueba otro distinto.");
            status = HttpStatus.CONFLICT;
        } else {
            message.put("internal_server_error","Error del servidor, intentelo más tarde.");
        }
        ApiErrorResponseJSON apiErrorResponseJSON = new ApiErrorResponseJSON(status, message);
        return ResponseEntity.status(status).body(apiErrorResponseJSON);
    }

    @ExceptionHandler(DataBaseConnectionException.class)
    public ResponseEntity<ApiErrorResponse> handleDataBaseConnectionException(DataBaseConnectionException e) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(apiErrorResponse);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidTokenException(Exception e) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiErrorResponse);
    }
}
