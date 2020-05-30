package ru.otus.spring.sagina.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.sagina.entity.Genre;

import java.util.List;
import java.util.UUID;

public interface GenreRepository extends JpaRepository<Genre, UUID> {
    List<Genre> findAllByIdIn(List<UUID> ids);
}
