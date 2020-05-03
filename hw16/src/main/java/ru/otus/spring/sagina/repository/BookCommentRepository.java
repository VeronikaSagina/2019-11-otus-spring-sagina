package ru.otus.spring.sagina.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.spring.sagina.entity.BookComment;

import java.util.List;

@RepositoryRestResource(path = "comment")
public interface BookCommentRepository extends CrudRepository<BookComment, Long> {
    @RestResource(path = "find", rel = "customFindAllByBookId")
    @Query(value = "select * from book_comment where book_id = :bookId", nativeQuery = true)
    List<BookComment> findAllByBookId(@Param("bookId") Long bookId);
}
