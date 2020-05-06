package ru.otus.spring.sagina.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.spring.sagina.TestLifecycle;
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.testdata.AuthorData;
import ru.otus.spring.sagina.utils.TestUtils;

import java.util.Comparator;
import java.util.List;

class AuthorRepositoryTest extends TestLifecycle {
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void getByNameTest() {
        List<Author> expected = List.of(
                new Author(AuthorData.SAPKOWSKI.getId(), AuthorData.SAPKOWSKI.getName()));
        List<Author> actual = authorRepository.findAllByNameContainingIgnoreCaseOrderByName("анджей");
        Assertions.assertTrue(TestUtils.compare(expected, actual,
                Comparator.comparing(Author::getId).thenComparing(Author::getName)));
    }

    @Test
    void getByNameTest2() {
        List<Author> actual = authorRepository.findAllByNameContainingIgnoreCaseOrderByName("п");
        List<Author> expected = List.of(AuthorData.SAPKOWSKI, AuthorData.PELEVIN);
        Assertions.assertTrue(TestUtils.compare(expected, actual,
                Comparator.comparing(Author::getId).thenComparing(Author::getName)));
    }
}
