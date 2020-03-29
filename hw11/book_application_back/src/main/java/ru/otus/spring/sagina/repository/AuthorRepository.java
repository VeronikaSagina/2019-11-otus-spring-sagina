package ru.otus.spring.sagina.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.spring.sagina.entity.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
}
