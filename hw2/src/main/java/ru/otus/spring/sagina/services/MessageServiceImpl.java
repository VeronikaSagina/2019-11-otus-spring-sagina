package ru.otus.spring.sagina.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class MessageServiceImpl implements MessageService{
    private final MessageSource messageSource;
    private LocaleService localeService;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public MessageServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setLocaleService(LocaleService localeService) {
        this.localeService = localeService;
    }

    public String getMessage(String code){
        return messageSource.getMessage(code, null, localeService.getLocale());
    }

    public String getMessage(String code, Object[] params){
        return messageSource.getMessage(code, params, localeService.getLocale());
    }

    public void writeMessage(String message) {
        System.out.println(message);
    }

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

    public String readMessageWithoutCheck(){
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeResources() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
