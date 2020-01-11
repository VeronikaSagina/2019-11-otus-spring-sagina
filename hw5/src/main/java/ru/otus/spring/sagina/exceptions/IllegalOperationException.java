package ru.otus.spring.sagina.exceptions;

public class IllegalOperationException extends ApplicationException {
    public IllegalOperationException(String message) {
        super(message);
    }
}
