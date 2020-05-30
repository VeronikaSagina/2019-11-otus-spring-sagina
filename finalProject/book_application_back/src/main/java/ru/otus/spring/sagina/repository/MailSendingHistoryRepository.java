package ru.otus.spring.sagina.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.sagina.entity.MailSendingHistory;

import java.util.UUID;

public interface MailSendingHistoryRepository extends JpaRepository<MailSendingHistory, UUID> {
}
