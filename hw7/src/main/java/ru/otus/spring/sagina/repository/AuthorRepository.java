package ru.otus.spring.sagina.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.sagina.entity.Author;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    List<Author> findAllByNameContainingIgnoreCaseOrderByName(String name);
}
