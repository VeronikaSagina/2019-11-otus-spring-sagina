package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocaleServiceImpl implements LocaleService {

    private final MessageService messageService;
    private Locale locale;

    public LocaleServiceImpl(MessageService messageService) {
        this.messageService = messageService;
    }

    public Locale getLocale() {
        if (locale == null) {
            setLocale();
        }
        return locale;
    }

    private void setLocale() {
        locale = Locale.getDefault();
        if (!(locale.equals(new Locale("ru")) || locale.equals(Locale.ENGLISH))) {
            locale = Locale.ENGLISH;
        }
        messageService.writeMessage(messageService.getMessage("language.select"));
        if (useDefaultLocale(messageService.getMessage("no"))) {
            locale = locale.equals(Locale.ENGLISH) ? new Locale("ru") : Locale.ENGLISH;
        }
        messageService.writeMessage(messageService.getMessage("language.message"));
    }

    private boolean useDefaultLocale(String no) {
        String answer = messageService.readMessageWithoutCheck();
        return no.equalsIgnoreCase(answer);
    }
}
