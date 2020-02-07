package ru.otus.spring.sagina.dao;

import ru.otus.spring.sagina.domain.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentDao {

    BookComment save(BookComment comment);

    Optional<BookComment> findById(int id);

    List<BookComment> getAllByBookId(int bookId);

    void delete(BookComment bookComment);

    void deleteAllByBookId(int bookId);

    void deleteById(int id);
}
