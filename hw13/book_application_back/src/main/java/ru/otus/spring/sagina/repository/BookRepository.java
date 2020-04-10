package ru.otus.spring.sagina.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.sagina.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
