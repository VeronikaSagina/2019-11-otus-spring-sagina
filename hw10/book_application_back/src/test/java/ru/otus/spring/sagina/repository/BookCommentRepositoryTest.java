package ru.otus.spring.sagina.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.spring.sagina.TestLifecycle;
import ru.otus.spring.sagina.entity.BookComment;
import ru.otus.spring.sagina.testdata.BookCommentData;
import ru.otus.spring.sagina.utils.TestUtils;

import java.util.Comparator;
import java.util.List;

class BookCommentRepositoryTest extends TestLifecycle {
    @Autowired
    private BookCommentRepository commentRepository;

    @Test
    void deleteAllByBookIdTest() {
        List<BookComment> forDelete = commentRepository.findAllByBookId("1");
        Assertions.assertFalse(forDelete.isEmpty());
        commentRepository.deleteAllByBookId("1");
        List<BookComment> actual = commentRepository.findAllByBookId("1");
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findAllByBookIdTest() {
        List<BookComment> actual = commentRepository.findAllByBookId("1");
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertTrue(TestUtils.compare(
                List.of(BookCommentData.KARENINA_COMMENT_1, BookCommentData.KARENINA_COMMENT_2), actual,
                Comparator.comparing(BookComment::getId)
                        .thenComparing(it -> it.getBook().getId())
                        .thenComparing(BookComment::getMessage)));
    }
}
