package ru.otus.spring.sagina.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.spring.sagina.entity.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
}
