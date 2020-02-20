package ru.otus.spring.sagina.repository;

import ru.otus.spring.sagina.entity.Book;

import java.util.List;

public interface BookCustomRepository {
    Book findFirstByGenreId(String genreId);

    List<Book> findAllByGenreId(String genreId);
}
