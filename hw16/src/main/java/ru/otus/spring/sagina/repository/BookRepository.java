package ru.otus.spring.sagina.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.spring.sagina.entity.Book;

import java.util.List;

@RepositoryRestResource(path = "book")
public interface BookRepository extends CrudRepository<Book, Long> {
    @RestResource(path = "byAuthor", rel = "customFindAllByAuthorId")
    List<Book> findAllByAuthorId(Long id);
}
