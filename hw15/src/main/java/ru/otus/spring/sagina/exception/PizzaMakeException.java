package ru.otus.spring.sagina.exception;

public class PizzaMakeException extends RuntimeException{
    public PizzaMakeException(String message) {
        super(message);
    }
}
