package edu.tus.winemanager.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ResourceExceptionController {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> exception(ResourceNotFoundException resourceNotFoundException) {
        return new ResponseEntity<>("Wine not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = CouldNotAddResourceException.class)
    public ResponseEntity<Object> exception(CouldNotAddResourceException couldNotAddResourceException) {
        return new ResponseEntity<>("Could Not Add Resource", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> exception(MethodArgumentNotValidException methodArgumentNotValidException) {
        Map<String, String> errors = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String error = "Malformed JSON request ";
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.message = ex.getMessage();
//        ApiErrorResponse response = new ApiErrorResponse.ApiErrorResponseBuilder().withStatus(status).withError_code("BAD_DATA").withMessage(ex.getLocalizedMessage()).withDetail(error + ex.getMessage()).build();
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

}
