package ru.otus.spring.sagina.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.spring.sagina.entity.Author;

import java.util.List;

@RepositoryRestResource(path = "author")
public interface AuthorRepository extends CrudRepository<Author, Long> {
    @RestResource(path = "byName", rel = "customFindMethod")
    List<Author> findAllByNameContainingIgnoreCaseOrderByName(String name);
}
