package ru.otus.spring.sagina.dao;

import ru.otus.spring.sagina.domain.Author;
import ru.otus.spring.sagina.domain.Book;

import java.util.List;

public interface BookDao {
    Book create(Book book);

    void update(Book book);

    void delete(List<Integer> bookIds);

    Book getById(int id);

    boolean existsById(int id);

    boolean existsByAuthorId(int authorId);

    Book getByTitle(String title);

    List<Book> getAll();

    List<Book> getAllByAuthor(Author author);

    List<Integer> getBookGenresIds(int bookId);

    List<Book> getBooksByGenreId(int genreId);

    List<Integer> getBooksIdByAuthorId(int authorId);
}
