package ru.otus.spring.sagina.dao;

import ru.otus.spring.sagina.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    Author save(Author author);

    void delete(Author author);

    Optional<Author> findById(int id);

    Optional<Author> findByIdWithBooks(int id);

    List<Author> getByName(String name);

    List<Author> getAll();
}
