package ru.otus.spring.sagina.repository;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.testdata.AuthorData;
import ru.otus.spring.sagina.utils.TestUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@DataJpaTest
class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void createTest() {
        Author expected = authorRepository.save(AuthorData.PUSHKIN);
        Assertions.assertEquals(6, authorRepository.findAll().size());
        Assertions.assertEquals(expected.getId(), authorRepository.findAll().get(5).getId());
        Assertions.assertEquals(expected.getName(), authorRepository.findAll().get(5).getName());
    }

    @Test
    void updateTest() {
        Author expected = authorRepository.save(new Author(5, "Джордж Мартин", List.of()));
        entityManager.flush();
        entityManager.detach(expected);
        Optional<Author> actual = authorRepository.findById(expected.getId());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected.getId(), actual.get().getId());
        Assertions.assertEquals(expected.getName(), actual.get().getName());
    }

    @Test
    void deleteTest() {
        Statistics statistics = getSessionStatistics();
        Optional<Author> forDelete = authorRepository.findById(AuthorData.PELEVIN.getId());
        Assertions.assertTrue(forDelete.isPresent());
        authorRepository.delete(forDelete.get());
        entityManager.flush();
        entityManager.detach(forDelete.get());
        List<Author> actual = authorRepository.findAll();
        Assertions.assertTrue(actual.stream()
                .noneMatch(a -> a.getId().equals(AuthorData.PELEVIN.getId())));
        Assertions.assertTrue(authorRepository.findById(AuthorData.PELEVIN.getId()).isEmpty());
        Assertions.assertTrue(bookRepository.findAllByAuthorIdIn(List.of(AuthorData.PELEVIN.getId())).isEmpty());
        Assertions.assertEquals(5, statistics.getPrepareStatementCount());
    }

    @Test
    void getByIdTest() {
        Statistics statistics = getSessionStatistics();

        Optional<Author> actual = authorRepository.findById(5);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(AuthorData.PELEVIN.getId(), actual.get().getId());
        Assertions.assertEquals(AuthorData.PELEVIN.getName(), actual.get().getName());
        Assertions.assertTrue(authorRepository.findById(6).isEmpty());

        Assertions.assertEquals(2, statistics.getPrepareStatementCount());
    }

    @Test
    void getByNameTest() {
        Statistics statistics = getSessionStatistics();

        List<Author> expected = List.of(
                new Author(AuthorData.SAPKOWSKI.getId(), AuthorData.SAPKOWSKI.getName(), List.of()));
        List<Author> actual = authorRepository.findAllByNameContainingIgnoreCaseOrderByName("анджей");
        Assertions.assertTrue(TestUtils.compare(expected, actual,
                Comparator.comparing(Author::getId).thenComparing(Author::getName)));

        Assertions.assertEquals(1, statistics.getPrepareStatementCount());
    }

    @Test
    void getByNameTest2() {
        Statistics statistics = getSessionStatistics();

        List<Author> actual = authorRepository.findAllByNameContainingIgnoreCaseOrderByName("п");
        List<Author> expected = List.of(AuthorData.SAPKOWSKI, AuthorData.PELEVIN);
        Assertions.assertTrue(TestUtils.compare(expected, actual,
                Comparator.comparing(Author::getId).thenComparing(Author::getName)));

        Assertions.assertEquals(1, statistics.getPrepareStatementCount());
    }

    @Test
    void getAllTest() {
        Statistics statistics = getSessionStatistics();

        List<Author> expected = Arrays.asList(
                AuthorData.TOLSTOY, AuthorData.SAPKOWSKI,
                AuthorData.CHRISTIE, AuthorData.TOLKIEN, AuthorData.PELEVIN);
        List<Author> actual = authorRepository.findAll();
        Assertions.assertTrue(TestUtils.compare(expected, actual,
                Comparator.comparing(Author::getId).thenComparing(Author::getName)));

        Assertions.assertEquals(1, statistics.getPrepareStatementCount());
    }

    private Statistics getSessionStatistics() {
        SessionFactory sessionFactory = entityManager.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        Statistics statistics = sessionFactory.getStatistics();
        statistics.clear();
        statistics.setStatisticsEnabled(true);
        return statistics;
    }
}
