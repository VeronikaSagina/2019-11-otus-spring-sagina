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
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.testdata.AuthorData;
import ru.otus.spring.sagina.testdata.InitDb;

import java.util.List;

@ExtendWith(SpringExtension.class)
@Import(InitDb.class)
@DataMongoTest
class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;
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
    void getByNameTest() {
        List<Author> expected = List.of(
                new Author(AuthorData.SAPKOWSKI.getId(), AuthorData.SAPKOWSKI.getName()));
        List<Author> actual = authorRepository.findAllByNameContainingIgnoreCaseOrderByName("анджей");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getByNameTest2() {
        List<Author> actual = authorRepository.findAllByNameContainingIgnoreCaseOrderByName("п");
        List<Author> expected = List.of(AuthorData.SAPKOWSKI, AuthorData.PELEVIN);
        Assertions.assertEquals(expected, actual);
    }
}
