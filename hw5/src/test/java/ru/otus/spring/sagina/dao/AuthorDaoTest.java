package ru.otus.spring.sagina.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import ru.otus.spring.sagina.domain.Author;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.testdata.AuthorData;

import java.util.Arrays;
import java.util.List;

@JdbcTest
@Import(AuthorDaoImpl.class)
class AuthorDaoTest {
    @Autowired
    private AuthorDaoImpl authorDao;

    @Test
    void createTest() {
        authorDao.create(AuthorData.PUSHKIN);
        List<Author> expected = Arrays.asList(
                AuthorData.TOLSTOY, AuthorData.SAPKOWSKI, AuthorData.CHRISTIE,
                AuthorData.TOLKIEN, AuthorData.PELEVIN, AuthorData.PUSHKIN);
        Assertions.assertEquals(expected, authorDao.getAll());
    }

    @Test
    void createTestNull() {
        Assertions.assertThrows(NullPointerException.class, () -> authorDao.create(new Author(6, null)));
    }

    @Test
    void createTestException() {
        Assertions.assertThrows(DataAccessException.class, () -> authorDao.create(AuthorData.PELEVIN));
    }

    @Test
    void updateTest() {
        Author expected = new Author(5, "Джордж Мартин");
        authorDao.update(expected);
        Assertions.assertEquals(expected, authorDao.getById(expected.getId()));
    }

    @Test
    void updateNothingTest() {
        Assertions.assertEquals(0, authorDao.update(new Author(6, "Джордж Мартин")));
    }

    @Test
    void deleteTest() {
        authorDao.delete(5);
        List<Author> expected = Arrays.asList(
                AuthorData.TOLSTOY, AuthorData.SAPKOWSKI,
                AuthorData.CHRISTIE, AuthorData.TOLKIEN);
        Assertions.assertEquals(expected, authorDao.getAll());
    }

    @Test
    void getByIdTest() {
        Assertions.assertEquals(AuthorData.PELEVIN, authorDao.getById(5));
    }

    @Test
    void getByIdTestException() {
        Assertions.assertThrows(NotFoundException.class, () -> authorDao.getById(6));
    }

    @Test
    void getByNameTest() {
        Assertions.assertEquals(List.of(AuthorData.SAPKOWSKI), authorDao.getByName("анджей"));
    }
    @Test
    void getByNameTest2() {
        Assertions.assertEquals(List.of(AuthorData.SAPKOWSKI, AuthorData.PELEVIN), authorDao.getByName("п"));
    }

    @Test
    void getAllTest() {
        List<Author> expected = Arrays.asList(
                AuthorData.TOLSTOY, AuthorData.SAPKOWSKI,
                AuthorData.CHRISTIE, AuthorData.TOLKIEN, AuthorData.PELEVIN);
        Assertions.assertEquals(expected, authorDao.getAll());
    }

    @Test
    void getIdFromSequenceTest() {
        Assertions.assertEquals(6, authorDao.getIdFromSequence());
    }

    @Test
    void existsByIdTest() {
        Assertions.assertTrue(authorDao.existsById(2));
    }

    @Test
    void notExistsByIdTest() {
        Assertions.assertFalse(authorDao.existsById(6));
    }
}
