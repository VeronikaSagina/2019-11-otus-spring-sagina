package ru.otus.spring.sagina.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.sagina.entity.Book;

public interface BookRepository extends MongoRepository<Book, String> {
}
