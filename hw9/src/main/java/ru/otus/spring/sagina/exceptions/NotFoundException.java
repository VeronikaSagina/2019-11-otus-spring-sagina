package ru.otus.spring.sagina.exceptions;

public class NotFoundException extends ApplicationException {
    public NotFoundException(String message) {
        super(message);
    }
}
