package ru.otus.spring.sagina.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.sagina.entity.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String>, BookCustomRepository {
    List<Book> findAllByAuthorIdIn(List<String> authorIds);

    List<Book> findAllByTitleContainingIgnoreCaseOrderByTitle(String title);
}
