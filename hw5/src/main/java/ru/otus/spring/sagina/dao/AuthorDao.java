package ru.otus.spring.sagina.dao;

import ru.otus.spring.sagina.domain.Author;

import java.util.List;

public interface AuthorDao {
    void create(Author author);

    int update(Author author);

    void delete(int authorId);

    Author getById(int id);

    List<Author> getByName(String name);

    List<Author> getAll();

    int getIdFromSequence();

    boolean existsById(Integer id);
}
