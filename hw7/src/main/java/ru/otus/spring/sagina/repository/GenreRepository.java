package ru.otus.spring.sagina.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.sagina.entity.Genre;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    List<Genre> findAllByIdIn(List<Integer> ids);

    List<Genre> findAllByTypeContainingIgnoreCaseOrderByType(String type);
}
