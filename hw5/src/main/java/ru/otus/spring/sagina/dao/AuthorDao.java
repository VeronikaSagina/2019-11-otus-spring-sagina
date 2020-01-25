package ru.otus.spring.sagina.dao;

import ru.otus.spring.sagina.domain.Author;

import java.util.List;

public interface AuthorDao {
    Author create(Author author);

    int update(Author author);

    void delete(int authorId);

    Author getById(int id);

    List<Author> getByName(String name);

    List<Author> getAll();

    boolean existsById(Integer id);
}
