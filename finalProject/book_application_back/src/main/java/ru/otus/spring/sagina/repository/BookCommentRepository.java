package ru.otus.spring.sagina.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.sagina.entity.BookComment;

import java.util.List;
import java.util.UUID;

public interface BookCommentRepository extends JpaRepository<BookComment, UUID> {
    List<BookComment> findAllByBookId(UUID bookId);
}
