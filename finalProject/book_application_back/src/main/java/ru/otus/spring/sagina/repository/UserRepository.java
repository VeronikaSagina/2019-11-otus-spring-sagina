package ru.otus.spring.sagina.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.sagina.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByLoginIgnoreCase(String login);

    @Query(value = "select * from app_user where app_user.consent_to_communication = true"
            + " and app_user.user_id not in"
            + " (select distinct(user_id) from sending_email_history where sending_date > :date)",
            nativeQuery = true)
    List<User> findAllForEmailSending(@Param("date") LocalDateTime date);
}