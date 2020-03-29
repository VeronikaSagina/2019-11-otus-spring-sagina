package ru.otus.spring.sagina.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.spring.sagina.dto.response.ErrorResponse;
import ru.otus.spring.sagina.exceptions.ApplicationException;
import ru.otus.spring.sagina.exceptions.NotFoundException;

import javax.validation.ValidationException;

@RestControllerAdvice
public class ControllerAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> exceptionHandle(Exception e) {
        return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e) {
        return response(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        return response(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleFailedConstraintException(MethodArgumentNotValidException e) {
        return response(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
        return response(e, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> response(Throwable t, HttpStatus status) {
        return new ResponseEntity<>(new ErrorResponse(status.value(), t.getMessage()), status);
    }
}
