package ru.otus.spring.sagina.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.sagina.entity.BookComment;

import java.util.List;

public interface BookCommentRepository extends MongoRepository<BookComment, String> {
    void deleteAllByBookId(String bookId);

    List<BookComment> findAllByBookId(String bookId);
}
