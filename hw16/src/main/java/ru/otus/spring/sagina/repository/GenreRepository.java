package ru.otus.spring.sagina.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.spring.sagina.entity.Genre;

@RepositoryRestResource(path = "genre")
public interface GenreRepository extends CrudRepository<Genre, Long> {
    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

    @Override
    @RestResource(exported = false)
    void delete(Genre genre);
}
