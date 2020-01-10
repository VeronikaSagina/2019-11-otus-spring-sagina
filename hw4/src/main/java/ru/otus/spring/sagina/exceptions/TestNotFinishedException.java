package ru.otus.spring.sagina.exceptions;

public class TestNotFinishedException extends RuntimeException {
    public TestNotFinishedException(String message) {
        super(message);
    }
}
