package ru.otus.spring.sagina.exceptions;

public class MinioException extends ApplicationException {
    public MinioException(String message) {
        super(message);
    }

    public MinioException(String message, Throwable cause) {
        super(message, cause);
    }
}
