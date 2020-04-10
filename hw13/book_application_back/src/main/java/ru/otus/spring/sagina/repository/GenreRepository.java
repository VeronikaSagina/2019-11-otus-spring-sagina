package ru.otus.spring.sagina.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.sagina.entity.Genre;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findAllByIdIn(List<Long> ids);
}
