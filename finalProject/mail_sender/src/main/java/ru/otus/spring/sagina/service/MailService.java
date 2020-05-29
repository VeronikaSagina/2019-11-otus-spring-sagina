package ru.otus.spring.sagina.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.otus.spring.sagina.dto.EmailDto;

import javax.mail.MessagingException;
import java.nio.charset.StandardCharsets;

@Service
public class MailService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MailService.class);
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final String template;
    private final String host;
    private final String from;

    public MailService(TemplateEngine templateEngine,
                       JavaMailSender mailSender,
                       @Value("${mail.template:mail_template.html}") String template,
                       @Value("${mail.host:http://localhost:3000}") String host,
                       @Value("${mail.from:sagina.veronika@yandex.ru}") String from) {
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
        this.template = template;
        this.host = host;
        this.from = from;
    }

    public void sendEmail(EmailDto emailDto) {
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(
                    mailSender.createMimeMessage(), true, StandardCharsets.UTF_8.name());
            messageHelper.setTo(emailDto.email);
            messageHelper.setFrom(from);
            messageHelper.setSubject("Новая книга от BookApp");
            messageHelper.setText(getText(emailDto), true);
            mailSender.send(messageHelper.getMimeMessage());
        }catch (MessagingException e) {
            LOGGER.error("ошибка отправки собщения на почту {}", emailDto.email, e);
        }
    }

    private String getText(EmailDto emailDto) {
        Context context = new Context();
        context.setVariable("host", host);
        context.setVariable("name", emailDto.name);
        context.setVariable("book_id", emailDto.bookInfo.id);
        context.setVariable("book_name", emailDto.bookInfo.name);
        context.setVariable("content", emailDto.bookInfo.content);
        return templateEngine.process(template, context);
    }
}
