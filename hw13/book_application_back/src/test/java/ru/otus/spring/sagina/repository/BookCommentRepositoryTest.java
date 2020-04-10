package ru.otus.spring.sagina.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.spring.sagina.entity.BookComment;
import ru.otus.spring.sagina.testdata.BookCommentData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
class BookCommentRepositoryTest {
    @Autowired
    private BookCommentRepository commentRepository;

    @Test
    void findAllByBookIdTest() {
        List<BookComment> actual = commentRepository.findAllByBookId(1L);
        assertFalse(actual.isEmpty());
        assertEquals(List.of(BookCommentData.KARENINA_COMMENT_1, BookCommentData.KARENINA_COMMENT_2), actual);
    }
}
