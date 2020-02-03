package ru.otus.spring.sagina.dao;

import ru.otus.spring.sagina.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Book save(Book book);

    void delete(Book book);

    Optional<Book> findById(int id);

    List<Book> getByIds(List<Integer>ids);

    List<Book> getByAuthorId(int authorId);

    List<Book> getByGenreId(int genreId);

    List<Book> getByTitle(String title);

    List<Book> getAll();
}
