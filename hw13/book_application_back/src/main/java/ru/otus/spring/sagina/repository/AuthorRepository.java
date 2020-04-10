package ru.otus.spring.sagina.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.sagina.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
