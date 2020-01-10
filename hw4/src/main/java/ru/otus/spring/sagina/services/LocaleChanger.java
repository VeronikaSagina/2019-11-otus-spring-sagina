package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocaleChanger {
    private final MessageService messageService;

    public LocaleChanger(MessageService messageService) {
        this.messageService = messageService;
    }

    public String setLocale(String locale) {
        Locale.setDefault("ru".equalsIgnoreCase(locale) ? new Locale("ru") : Locale.ENGLISH);
        return messageService.getMessage("language.message");
    }
}
