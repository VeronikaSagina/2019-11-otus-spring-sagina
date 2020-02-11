package ru.otus.spring.sagina.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.sagina.entity.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    @EntityGraph(attributePaths = {"author", "genres"})
    List<Book> findAll();

    @EntityGraph(attributePaths = {"author", "genres"})
    List<Book> findAllByAuthorIdIn(List<Integer> authorIds);

    @EntityGraph(attributePaths = {"author", "genres"})
    @Query("select b from Book b join b.genres g where g.id = :genreId order by b.title")
    List<Book> findAllByGenreId(@Param("genreId") int genreId);

    @EntityGraph(attributePaths = {"author", "genres"})
    List<Book> findAllByTitleContainingIgnoreCaseOrderByTitle(String title);
}
