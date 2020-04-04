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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.spring.sagina.InitDb;
import ru.otus.spring.sagina.entity.BookComment;
import ru.otus.spring.sagina.testdata.BookCommentData;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@Import(InitDb.class)
class BookCommentRepositoryTest {
    @Autowired
    private BookCommentRepository commentRepository;
    @Autowired
    private InitDb initDb;

    @BeforeEach
    void initDb() {
        initDb.initDb();
    }

    @AfterEach
    void cleanDb() {
        initDb.cleanDb();
    }

    @Test
    void createTest() {
        Mono<BookComment> created = commentRepository.save(BookCommentData.NEW_COMMENT);
        StepVerifier
                .create(created)
                .assertNext(comment -> Assertions.assertNotNull(comment.getId()))
                .expectComplete()
                .verify();
    }

    @Test
    void findAllByBookIdTest() {
        StepVerifier
                .create(commentRepository.findAllByBookId("1"))
                .expectNext(BookCommentData.KARENINA_COMMENT_1)
                .expectNext(BookCommentData.KARENINA_COMMENT_2)
                .expectComplete()
                .verify();
    }

    @Test
    void deleteAllByBookIdTest() {
        StepVerifier
                .create(commentRepository.findAllByBookId("1"))
                .expectNextCount(2)
                .then(() -> commentRepository.deleteAllByBookId("1"))
                .then(() -> commentRepository.findAllByBookId("1"))
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }
}
