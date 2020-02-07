package ru.otus.spring.sagina.dao;

import ru.otus.spring.sagina.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    Genre save(Genre genre);

    Optional<Genre> findById(int id);

    List<Genre> getByIds(List<Integer> ids);

    List<Genre> getByType(String type);

    List<Genre> getAll();
}
