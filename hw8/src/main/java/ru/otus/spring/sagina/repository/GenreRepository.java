package ru.otus.spring.sagina.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.sagina.entity.Genre;

import java.util.List;

public interface GenreRepository extends MongoRepository<Genre, String> {
    List<Genre> findAllByIdIn(List<String> ids);

    List<Genre> findAllByTypeContainingIgnoreCaseOrderByType(String type);
}
