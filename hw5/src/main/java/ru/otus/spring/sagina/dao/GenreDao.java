package ru.otus.spring.sagina.dao;

import ru.otus.spring.sagina.domain.Genre;

import java.util.List;
import java.util.Set;

public interface GenreDao {
    Genre create(Genre genre);

    int update(Genre genre);

    Genre getById(int id);

    Set<Genre> getByIds(List<Integer> ids);

    List<Genre> getByType(String type);

    List<Genre> getAll();

    boolean existsById(int id);
}
