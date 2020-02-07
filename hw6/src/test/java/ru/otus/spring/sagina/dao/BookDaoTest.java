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
import ru.otus.spring.sagina.domain.Book;
import ru.otus.spring.sagina.domain.Genre;
import ru.otus.spring.sagina.testdata.AuthorData;
import ru.otus.spring.sagina.testdata.BookData;
import ru.otus.spring.sagina.testdata.GenreData;
import ru.otus.spring.sagina.utils.TestUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@DataJpaTest
@Import(BookDaoImpl.class)
class BookDaoTest {
    @Autowired
    private BookDaoImpl bookDao;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void createTest() {
        Book book = new Book(BookData.SNUFF.getTitle(), BookData.SNUFF.getAuthor());
        book.getGenres().add(GenreData.FANTASTIC);
        Book expected = bookDao.save(book);
        List<Book> expectedList = Arrays.asList(
                BookData.ANNA_KARENINA, BookData.BLOOD_OF_ELVES, BookData.ORIENT_EXPRESS,
                BookData.LORD_OF_THE_RINGS, BookData.SNUFF);
        Optional<Book> actual = bookDao.findById(5);
        List<Book> actualList = bookDao.getAll();
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertTrue(TestUtils.compare(expectedList, actualList,
                Comparator.comparing(Book::getId).thenComparing(Book::getTitle)));
        Assertions.assertEquals(AuthorData.PELEVIN.getId(), actual.get().getAuthor().getId());
        Assertions.assertEquals(AuthorData.PELEVIN.getName(), actual.get().getAuthor().getName());
        Assertions.assertEquals(expected.getId(), actual.get().getId());
        Assertions.assertEquals(expected.getTitle(), actual.get().getTitle());
    }

    @Test
    void updateTest() {
        Book forUpdate = new Book(1, "Анна убитая", AuthorData.TOLSTOY);
        forUpdate.getGenres().add(GenreData.NOVEL);
        Book expected = bookDao.save(forUpdate);
        List<Book> expectedList = Arrays.asList(
                expected, BookData.BLOOD_OF_ELVES,
                BookData.ORIENT_EXPRESS, BookData.LORD_OF_THE_RINGS);
        entityManager.flush();
        entityManager.detach(expected);
        Optional<Book> actual = bookDao.findById(1);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected.getTitle(), actual.get().getTitle());
        Assertions.assertEquals(expected.getId(), actual.get().getId());
        Assertions.assertEquals(expected.getAuthor().getId(), actual.get().getAuthor().getId());
        Assertions.assertEquals(expected.getAuthor().getName(), actual.get().getAuthor().getName());
        Assertions.assertTrue(TestUtils.compare(expectedList, bookDao.getAll(),
                Comparator.comparing(Book::getId).thenComparing(Book::getTitle)
                        .thenComparing(b -> b.getAuthor().getId())));
        Assertions.assertEquals(1, actual.get().getGenres().size());
        Assertions.assertEquals(1, actual.get().getGenres().get(0).getId());
    }

    @Test
    void update2Test() {
        Book forUpdate = new Book(1, "Анна убитая", AuthorData.TOLSTOY);
        List<Genre> genres = List.of(GenreData.DETECTIVE, GenreData.FANTASY, GenreData.FANTASTIC);
        forUpdate.getGenres().addAll(genres);
        Book expected = bookDao.save(forUpdate);
        entityManager.flush();
        entityManager.detach(expected);
        Optional<Book> actual = bookDao.findById(1);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected.getId(), actual.get().getId());
        Assertions.assertEquals(expected.getTitle(), actual.get().getTitle());
        Assertions.assertEquals(expected.getAuthor().getId(), actual.get().getAuthor().getId());
        Assertions.assertEquals(expected.getAuthor().getName(), actual.get().getAuthor().getName());
        Assertions.assertEquals(genres.size(), actual.get().getGenres().size());
    }

    @Test
    void deleteTest() {
        Integer bookId = BookData.ANNA_KARENINA.getId();
        Optional<Book> forDelete = bookDao.findById(bookId);
        Assertions.assertTrue(forDelete.isPresent());
        Statistics statistics = getSessionStatistics();
        bookDao.delete(forDelete.get());
        entityManager.flush();
        entityManager.detach(forDelete.get());
        Assertions.assertEquals(4, statistics.getPrepareStatementCount());
        Assertions.assertTrue(bookDao.findById(bookId).isEmpty());
    }

    @Test
    void getByIdTest() {
        Optional<Book> actual = bookDao.findById(1);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getId(), actual.get().getId());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getTitle(), actual.get().getTitle());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getId(), actual.get().getAuthor().getId());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getName(), actual.get().getAuthor().getName());
        Assertions.assertEquals(1, actual.get().getGenres().size());
        Assertions.assertEquals(GenreData.NOVEL.getId(), actual.get().getGenres().get(0).getId());
        Assertions.assertEquals(GenreData.NOVEL.getType(), actual.get().getGenres().get(0).getType());
        Assertions.assertEquals(Optional.empty(), bookDao.findById(6));
    }

    @Test
    void getByAuthorIdTest() {
        List<Book> actual = bookDao.getByAuthorId(3);
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(BookData.ORIENT_EXPRESS.getAuthor().getId(), actual.get(0).getAuthor().getId());
        Assertions.assertEquals(BookData.ORIENT_EXPRESS.getTitle(), actual.get(0).getTitle());
        Assertions.assertEquals(BookData.ORIENT_EXPRESS.getId(), actual.get(0).getId());
        Assertions.assertEquals(List.of(), bookDao.getByAuthorId(6));
    }

    @Test
    void getByTitleTest() {
        Statistics statistics = getSessionStatistics();

        List<Book> actual = bookDao.getByTitle("эльф");
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(BookData.BLOOD_OF_ELVES.getId(), actual.get(0).getId());
        Assertions.assertEquals(BookData.BLOOD_OF_ELVES.getAuthor().getId(), actual.get(0).getAuthor().getId());
        Assertions.assertEquals(BookData.BLOOD_OF_ELVES.getTitle(), actual.get(0).getTitle());

        Assertions.assertEquals(1, statistics.getPrepareStatementCount());
    }

    @Test
    void getAllTest() {
        Statistics statistics = getSessionStatistics();
        List<Book> expected = Arrays.asList(
                BookData.ANNA_KARENINA, BookData.BLOOD_OF_ELVES,
                BookData.ORIENT_EXPRESS, BookData.LORD_OF_THE_RINGS);
        List<Book> books = bookDao.getAll();
        Assertions.assertTrue(TestUtils.compare(expected, books,
                Comparator.comparing(Book::getId).thenComparing(Book::getTitle)
                        .thenComparing(b -> b.getAuthor().getId())));
        Assertions.assertEquals(1, statistics.getPrepareStatementCount());
    }

    @Test
    void getBooksByGenreId() {
        List<Book> actual = bookDao.getByGenreId(3);
        Assertions.assertTrue(TestUtils.compare(List.of(BookData.BLOOD_OF_ELVES), actual,
                Comparator.comparing(Book::getId).thenComparing(Book::getTitle)
                        .thenComparing(b -> b.getAuthor().getId())));
        Assertions.assertTrue(TestUtils.compare(List.of(GenreData.FANTASY), actual.get(0).getGenres(),
                Comparator.comparing(Genre::getId).thenComparing(Genre::getType)));
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
