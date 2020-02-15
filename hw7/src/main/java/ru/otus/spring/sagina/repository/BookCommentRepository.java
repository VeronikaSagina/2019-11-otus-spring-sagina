package ru.otus.spring.sagina.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.sagina.entity.BookComment;

import java.util.List;

public interface BookCommentRepository extends JpaRepository<BookComment, Integer> {
    @Modifying
    @Query("delete from BookComment where book.id in (:bookId)")
    void deleteAllByBookIdIn(@Param("bookId") List<Integer> bookId);
}
