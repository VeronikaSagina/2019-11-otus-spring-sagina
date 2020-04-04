package ru.otus.spring.sagina.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.sagina.entity.BookComment;
import ru.otus.spring.sagina.testdata.BookCommentData;
import ru.otus.spring.sagina.testdata.InitDb;

import java.util.List;

@ExtendWith(SpringExtension.class)
@Import(InitDb.class)
@DataMongoTest
class BookCommentRepositoryTest {
    @Autowired
    private BookCommentRepository commentRepository;
    @Autowired
    private InitDb initDb;

    @BeforeEach
    void setup() {
        initDb.init();
    }

    @AfterEach
    void clean() {
        initDb.clean();
    }

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
        Assertions.assertEquals(List.of(BookCommentData.KARENINA_COMMENT_1, BookCommentData.KARENINA_COMMENT_2), actual);
    }
}
