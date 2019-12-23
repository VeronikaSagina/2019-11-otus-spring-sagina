package ru.otus.spring.sagina.services;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageSource messageSource;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public MessageServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault());
    }

    @Override
    public String getMessage(String code, Object[] params) {
        return messageSource.getMessage(code, params, Locale.getDefault());
    }

    @Override
    public void writeMessage(String message) {
        System.out.println(message);
    }

    @Override
    public String readMessage() {
        try {
            String message = "";
            while (message.isBlank()) {
                message = reader.readLine();
                if (message.isBlank()) {
                    writeMessage(getMessage("repeat_input"));
                }
            }
            return message;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String readMessageWithoutCheck() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    private void closeResources() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
