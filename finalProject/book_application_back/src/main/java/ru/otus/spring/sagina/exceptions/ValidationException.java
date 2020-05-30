package ru.otus.spring.sagina.exceptions;

public class ValidationException extends ApplicationException {
    public ValidationException(String message) {
        super(message);
    }
}
