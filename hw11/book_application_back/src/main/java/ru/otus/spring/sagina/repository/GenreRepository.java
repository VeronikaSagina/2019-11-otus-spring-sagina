package ru.otus.spring.sagina.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.spring.sagina.entity.Genre;

import java.util.List;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {
    Flux<Genre> findAllByIdIn(List<String> ids);
}
