package ru.otus.spring.sagina.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;
import ru.otus.spring.sagina.InitDb;
import ru.otus.spring.sagina.testdata.AuthorData;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@Import(InitDb.class)
class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;
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
    void findAllTest() {
        StepVerifier
                .create(authorRepository.findAll(Sort.by("name")))
                .expectNext(AuthorData.CHRISTIE)
                .expectNext(AuthorData.SAPKOWSKI)
                .expectNext(AuthorData.PELEVIN)
                .expectNext(AuthorData.TOLKIEN)
                .expectNext(AuthorData.TOLSTOY)
                .expectComplete()
                .verify();
    }
}
