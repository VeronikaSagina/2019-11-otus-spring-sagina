package ru.otus.spring.sagina.exceptions;

public class InvalidJwtTokenException extends AuthenticationException {
    public InvalidJwtTokenException(String message) {
        super(message);
    }

    public InvalidJwtTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
