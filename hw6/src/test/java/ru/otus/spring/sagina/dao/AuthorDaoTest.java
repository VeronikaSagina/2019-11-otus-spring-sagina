package ru.otus.spring.sagina.dao;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.spring.sagina.domain.Author;
import ru.otus.spring.sagina.testdata.AuthorData;
import ru.otus.spring.sagina.utils.TestUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@DataJpaTest
@Import({AuthorDaoImpl.class, BookDaoImpl.class})
class AuthorDaoTest {
    @Autowired
    private AuthorDaoImpl authorDao;

    @Autowired
    private BookDaoImpl bookDao;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void createTest() {
        Author expected = authorDao.save(AuthorData.PUSHKIN);
        Assertions.assertEquals(6, authorDao.getAll().size());
        Assertions.assertEquals(expected.getId(), authorDao.getAll().get(5).getId());
        Assertions.assertEquals(expected.getName(), authorDao.getAll().get(5).getName());
    }

    @Test
    void updateTest() {
        Author expected = authorDao.save(new Author(5, "Джордж Мартин"));
        entityManager.flush();
        entityManager.detach(expected);
        Optional<Author> actual = authorDao.findById(expected.getId());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected.getId(), actual.get().getId());
        Assertions.assertEquals(expected.getName(), actual.get().getName());
    }

    @Test
    void deleteTest() {
        Statistics statistics = getSessionStatistics();
        Optional<Author> forDelete = authorDao.findById(AuthorData.PELEVIN.getId());
        Assertions.assertTrue(forDelete.isPresent());
        authorDao.delete(forDelete.get());
        entityManager.flush();
        entityManager.detach(forDelete.get());
        List<Author> actual = authorDao.getAll();
        Assertions.assertTrue(actual.stream()
                .noneMatch(a -> a.getId().equals(AuthorData.PELEVIN.getId())));
        Assertions.assertTrue(authorDao.findById(AuthorData.PELEVIN.getId()).isEmpty());
        Assertions.assertTrue(bookDao.getByAuthorId(AuthorData.PELEVIN.getId()).isEmpty());
        Assertions.assertEquals(9, statistics.getPrepareStatementCount());
    }

    @Test
    void getByIdTest() {
        Statistics statistics = getSessionStatistics();

        Optional<Author> actual = authorDao.findById(5);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(AuthorData.PELEVIN.getId(), actual.get().getId());
        Assertions.assertEquals(AuthorData.PELEVIN.getName(), actual.get().getName());
        Assertions.assertTrue(authorDao.findById(6).isEmpty());

        Assertions.assertEquals(2, statistics.getPrepareStatementCount());
    }

    @Test
    void getByNameTest() {
        Statistics statistics = getSessionStatistics();

        List<Author> expected = List.of(new Author(AuthorData.SAPKOWSKI.getId(), AuthorData.SAPKOWSKI.getName()));
        List<Author> actual = authorDao.getByName("анджей");
        Assertions.assertTrue(TestUtils.compare(expected, actual,
                Comparator.comparing(Author::getId).thenComparing(Author::getName)));

        Assertions.assertEquals(1, statistics.getPrepareStatementCount());
    }

    @Test
    void getByNameTest2() {
        Statistics statistics = getSessionStatistics();

        List<Author> actual = authorDao.getByName("п");
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
        List<Author> actual = authorDao.getAll();
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
