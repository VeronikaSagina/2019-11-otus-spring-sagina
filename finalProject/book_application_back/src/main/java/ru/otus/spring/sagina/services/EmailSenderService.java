package ru.otus.spring.sagina.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dto.kafka.BookInfoDto;
import ru.otus.spring.sagina.dto.kafka.EmailDto;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.entity.MailSendingHistory;
import ru.otus.spring.sagina.entity.User;
import ru.otus.spring.sagina.repository.BookRepository;
import ru.otus.spring.sagina.repository.MailSendingHistoryRepository;
import ru.otus.spring.sagina.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EmailSenderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSender.class);
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final MailSendingHistoryRepository mailSendingHistoryRepository;
    private final KafkaSender kafkaSender;

    public EmailSenderService(BookRepository bookRepository,
                              UserRepository userRepository,
                              MailSendingHistoryRepository mailSendingHistoryRepository,
                              KafkaSender kafkaSender) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.mailSendingHistoryRepository = mailSendingHistoryRepository;
        this.kafkaSender = kafkaSender;
    }

    public void sendEmail(UUID bookId) {
        List<User> forSending = userRepository.findAllForEmailSending(LocalDateTime.now().minusDays(7));
        Book book = bookRepository.findById(bookId).orElseThrow();
        forSending.forEach(it -> {
                    EmailDto emailDto = new EmailDto(it.getEmail(), it.getLogin(),
                            new BookInfoDto(bookId, book.getName(), book.getDescription()));
                    try {
                        kafkaSender.sendTask(emailDto);
                        var historyItem = new MailSendingHistory();
                        historyItem.setSendingDate(LocalDateTime.now());
                        historyItem.setUserId(it.getId());
                        mailSendingHistoryRepository.save(historyItem);
                    } catch (Exception e) {
                        LOGGER.error("ошибка отправки сообщения в кафку [{}]", emailDto, e);
                    }
                }
        );
    }
}
