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
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.entity.Genre;
import ru.otus.spring.sagina.testdata.AuthorData;
import ru.otus.spring.sagina.testdata.BookData;
import ru.otus.spring.sagina.testdata.GenreData;
import ru.otus.spring.sagina.utils.TestUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void createTest() {
        Book book = new Book();
        book.setAuthor(BookData.SNUFF.getAuthor());
        book.setTitle(BookData.SNUFF.getTitle());
        book.getGenres().add(GenreData.FANTASTIC);
        Book expected = bookRepository.save(book);
        List<Book> expectedList = Arrays.asList(
                BookData.ANNA_KARENINA, BookData.BLOOD_OF_ELVES, BookData.ORIENT_EXPRESS,
                BookData.LORD_OF_THE_RINGS, BookData.SNUFF);
        Optional<Book> actual = bookRepository.findById(5);
        List<Book> actualList = bookRepository.findAll();
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
        Book forUpdate = BookData.getBookForAnnaKarenina();
        forUpdate.setTitle("Анна убитая");
        forUpdate.setGenres(List.of(GenreData.NOVEL));
        Book expected = bookRepository.save(forUpdate);
        List<Book> expectedList = Arrays.asList(
                expected, BookData.BLOOD_OF_ELVES,
                BookData.ORIENT_EXPRESS, BookData.LORD_OF_THE_RINGS);
        entityManager.flush();
        entityManager.detach(expected);
        Optional<Book> actual = bookRepository.findById(1);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected.getTitle(), actual.get().getTitle());
        Assertions.assertEquals(expected.getId(), actual.get().getId());
        Assertions.assertEquals(expected.getAuthor().getId(), actual.get().getAuthor().getId());
        Assertions.assertEquals(expected.getAuthor().getName(), actual.get().getAuthor().getName());
        Assertions.assertTrue(TestUtils.compare(expectedList, bookRepository.findAll(),
                Comparator.comparing(Book::getId).thenComparing(Book::getTitle)
                        .thenComparing(b -> b.getAuthor().getId())));
        Assertions.assertEquals(1, actual.get().getGenres().size());
        Assertions.assertEquals(1, actual.get().getGenres().get(0).getId());
    }

    @Test
    void update2Test() {
        Book forUpdate = BookData.getBookForAnnaKarenina();
        forUpdate.setTitle("Анна убитая");
        List<Genre> genres = List.of(GenreData.DETECTIVE, GenreData.FANTASY, GenreData.FANTASTIC);
        forUpdate.setGenres(genres);
        Book expected = bookRepository.save(forUpdate);
        entityManager.flush();
        entityManager.detach(expected);
        Optional<Book> actual = bookRepository.findById(1);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected.getId(), actual.get().getId());
        Assertions.assertEquals(expected.getTitle(), actual.get().getTitle());
        Assertions.assertEquals(expected.getAuthor().getId(), actual.get().getAuthor().getId());
        Assertions.assertEquals(expected.getAuthor().getName(), actual.get().getAuthor().getName());
        Assertions.assertEquals(genres.size(), actual.get().getGenres().size());
    }

    @Test
    void getByAuthorIdTest() {
        List<Book> actual = bookRepository.findAllByAuthorIdIn(List.of(3));
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(BookData.ORIENT_EXPRESS.getAuthor().getId(), actual.get(0).getAuthor().getId());
        Assertions.assertEquals(BookData.ORIENT_EXPRESS.getTitle(), actual.get(0).getTitle());
        Assertions.assertEquals(BookData.ORIENT_EXPRESS.getId(), actual.get(0).getId());
        Assertions.assertEquals(List.of(), bookRepository.findAllByAuthorIdIn(List.of(6)));
    }

    @Test
    void getByTitleTest() {
        Statistics statistics = getSessionStatistics();
        List<Book> actual = bookRepository.findAllByTitleContainingIgnoreCaseOrderByTitle("эльф");
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(BookData.BLOOD_OF_ELVES.getId(), actual.get(0).getId());
        Assertions.assertEquals(BookData.BLOOD_OF_ELVES.getAuthor().getId(), actual.get(0).getAuthor().getId());
        Assertions.assertEquals(BookData.BLOOD_OF_ELVES.getTitle(), actual.get(0).getTitle());

        Assertions.assertEquals(1, statistics.getPrepareStatementCount());
    }

    @Test
    void getBooksByGenreId() {
        List<Book> actual = bookRepository.findAllByGenreId(3);
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
