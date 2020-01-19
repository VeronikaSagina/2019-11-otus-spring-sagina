package ru.otus.spring.sagina.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.sagina.domain.Book;
import ru.otus.spring.sagina.testdata.AuthorData;
import ru.otus.spring.sagina.testdata.BookData;
import ru.otus.spring.sagina.testdata.GenreData;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@JdbcTest
@Import(BookDaoImpl.class)
class BookDaoTest {
    @Autowired
    private BookDaoImpl bookDao;

    @Test
    void createTest() {
        bookDao.create(BookData.SNUFF);
        List<Book> expected = Arrays.asList(
                BookData.ANNA_KARENINA, BookData.BLOOD_OF_ELVES, BookData.ORIENT_EXPRESS,
                BookData.LORD_OF_THE_RINGS, BookData.SNUFF);
        Book actual = bookDao.getById(5);
        Assertions.assertEquals(expected, bookDao.getAll());
        Assertions.assertEquals(AuthorData.PELEVIN, actual.getAuthor());
        Assertions.assertEquals(BookData.SNUFF, actual);
    }

    @Test
    void updateTest() {
        bookDao.update(new Book(1, "Анна убитая", null));
        Book updated = new Book(1, "Анна убитая", AuthorData.TOLSTOY);
        List<Book> expectedList = Arrays.asList(
                updated, BookData.BLOOD_OF_ELVES,
                BookData.ORIENT_EXPRESS, BookData.LORD_OF_THE_RINGS);
        Assertions.assertEquals(updated, bookDao.getById(1));
        Assertions.assertEquals(expectedList, bookDao.getAll());
        Assertions.assertEquals(List.of(1), bookDao.getBookGenresIds(updated.getId()));
    }

    @Test
    void update2Test() {
        Book forUpdate = new Book(1, "Анна убитая", null);
        forUpdate.getGenres().addAll(Set.of(GenreData.DETECTIVE, GenreData.FANTASY, GenreData.FANTASTIC));
        bookDao.update(forUpdate);
        Book updated = new Book(1, "Анна убитая", AuthorData.TOLSTOY);
        List<Book> expectedList = Arrays.asList(
                updated, BookData.BLOOD_OF_ELVES,
                BookData.ORIENT_EXPRESS, BookData.LORD_OF_THE_RINGS);
        Assertions.assertEquals(updated, bookDao.getById(1));
        Assertions.assertEquals(expectedList, bookDao.getAll());
        Assertions.assertEquals(List.of(2, 3, 4), bookDao.getBookGenresIds(updated.getId()));
    }

    @Test
    void deleteTest() {
        bookDao.delete(List.of(1, 2, 3));
        Assertions.assertEquals(List.of(BookData.LORD_OF_THE_RINGS), bookDao.getAll());
    }

    @Test
    void getByIdTest() {
        Assertions.assertEquals(BookData.ANNA_KARENINA, bookDao.getById(1));
    }

    @Test
    void existsByIdTest() {
        Assertions.assertTrue(bookDao.existsById(3));
    }

    @Test
    void notExistsByIdTest() {
        Assertions.assertFalse(bookDao.existsById(17));
    }

    @Test
    void existsByAuthorIdTest() {
        Assertions.assertTrue(bookDao.existsByAuthorId(3));
    }

    @Test
    void getByTitleTest() {
        Assertions.assertEquals(BookData.BLOOD_OF_ELVES, bookDao.getByTitle("эльф"));
    }

    @Test
    void getAllTest() {
        List<Book> expected = Arrays.asList(
                BookData.ANNA_KARENINA, BookData.BLOOD_OF_ELVES,
                BookData.ORIENT_EXPRESS, BookData.LORD_OF_THE_RINGS);
        Assertions.assertEquals(expected, bookDao.getAll());
    }

    @Test
    void getAllByAuthorTest() {
        Assertions.assertEquals(List.of(BookData.ANNA_KARENINA), bookDao.getAllByAuthor(AuthorData.TOLSTOY));
    }

    @Test
    void getBookGenresIdsTest() {
        Assertions.assertEquals(List.of(GenreData.FANTASY.getId()), bookDao.getBookGenresIds(2));
    }

    @Test
    void getBooksIdByAuthorIdTest() {
        Assertions.assertEquals(List.of(1), bookDao.getBooksIdByAuthorId(1));
    }

    @Test
    void getBooksByGenreId() {
        Assertions.assertEquals(List.of(BookData.BLOOD_OF_ELVES), bookDao.getBooksByGenreId(3));
    }
}
