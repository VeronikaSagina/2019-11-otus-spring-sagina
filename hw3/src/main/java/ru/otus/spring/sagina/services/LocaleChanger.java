package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocaleChanger {
    private final MessageService messageService;

    public LocaleChanger(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setLocale() {
        if (!(Locale.getDefault().equals(new Locale("ru"))
                || Locale.getDefault().equals(Locale.ENGLISH))) {
            Locale.setDefault(Locale.ENGLISH);
        }
        messageService.writeMessage(messageService.getMessage("language.select"));

        if (useDefaultLocale(messageService.getMessage("no"))) {
            if (Locale.getDefault().equals(Locale.ENGLISH)) {
                Locale.setDefault(new Locale("ru"));
            } else {
                Locale.setDefault(Locale.ENGLISH);
            }
        }
        messageService.writeMessage(messageService.getMessage("language.message"));
    }

    private boolean useDefaultLocale(String no) {
        return no.equalsIgnoreCase(messageService.readMessageWithoutCheck());
    }
}
