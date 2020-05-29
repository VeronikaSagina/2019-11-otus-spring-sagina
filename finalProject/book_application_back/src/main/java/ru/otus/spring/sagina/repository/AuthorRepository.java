package ru.otus.spring.sagina.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.sagina.entity.Author;

import java.util.List;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
    List<Author> findAllByNameContainingIgnoreCase(String name, Sort by);
}
