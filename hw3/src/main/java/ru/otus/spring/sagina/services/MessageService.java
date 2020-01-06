package ru.otus.spring.sagina.services;

public interface MessageService {
    String getMessage(String code);

    String getMessage(String code, Object[] params);

    String readMessage();

    String readMessageWithoutCheck();

    void writeMessage(String message);
}
