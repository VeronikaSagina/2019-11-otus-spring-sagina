package ru.otus.spring.sagina.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.sagina.entity.Book;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    @Query("select b from Book b where (:query = '' or lower(b.name) like lower(CONCAT('%', :query, '%')))"
            + " and (b.author.id = :authorId"
            + " or cast(:authorId as org.hibernate.type.UUIDCharType) is null) order by b.name")
    List<Book> findAllByNameAndAuthorId(@Param("authorId") UUID authorId, @Param("query") String query, Sort sort);

    List<Book> findAllByAuthorId(UUID id);
}
