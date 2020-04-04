package ru.otus.spring.sagina.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.testdata.AuthorData;
import ru.otus.spring.sagina.testdata.BookData;
import ru.otus.spring.sagina.testdata.GenreData;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@Import(InitDb.class)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
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
        StepVerifier
                .create(bookRepository.save(new Book(null, "Война и мир", "ОПИСАНИЕ",
                        AuthorData.TOLSTOY, List.of(GenreData.NOVEL), null)))
                .assertNext(book -> Assertions.assertNotNull(book.getId()))
                .expectComplete()
                .verify();
    }

    @Test
    void updateTest() {
        Book updated = BookData.getBookForAnnaKarenina();
        updated.setDescription("description");
        updated.setGenres(List.of(GenreData.NOVEL));
        StepVerifier
                .create(bookRepository.save(updated))
                .assertNext(book -> {
                    Assertions.assertEquals(updated.getDescription(), book.getDescription());
                    Assertions.assertEquals(BookData.ANNA_KARENINA.getId(), book.getId());
                    Assertions.assertEquals(BookData.ANNA_KARENINA.getName(), book.getName());
                    Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor(), book.getAuthor());
                    Assertions.assertEquals(updated.getGenres(), book.getGenres());
                })
                .expectComplete()
                .verify();
    }

    @Test
    void deleteBook() {
        StepVerifier
                .create(bookRepository.findById("1"))
                .expectNextCount(1)
                .then(() -> bookRepository.deleteById("1"))
                .then(() -> bookRepository.findById("1"))
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }

    @Test
    void getBookByIdTest() {
        StepVerifier
                .create(bookRepository.findById("1"))
                .assertNext(book -> {
                    Assertions.assertEquals(BookData.ANNA_KARENINA.getId(), book.getId());
                    Assertions.assertEquals(BookData.ANNA_KARENINA.getName(), book.getName());
                    Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor(), book.getAuthor());
                })
                .expectComplete()
                .verify();
    }

    @Test
    void getListBookTest() {
        StepVerifier
                .create(bookRepository.findAll(Sort.by("name")))
                .assertNext(book -> {
                    Assertions.assertEquals(BookData.ANNA_KARENINA.getId(), book.getId());
                    Assertions.assertEquals(BookData.ANNA_KARENINA.getName(), book.getName());
                    Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor(), book.getAuthor());
                })
                .assertNext(book -> {
                    Assertions.assertEquals(BookData.LORD_OF_THE_RINGS.getId(), book.getId());
                    Assertions.assertEquals(BookData.LORD_OF_THE_RINGS.getName(), book.getName());
                    Assertions.assertEquals(BookData.LORD_OF_THE_RINGS.getAuthor(), book.getAuthor());
                })
                .assertNext(book -> {
                    Assertions.assertEquals(BookData.BLOOD_OF_ELVES.getId(), book.getId());
                    Assertions.assertEquals(BookData.BLOOD_OF_ELVES.getName(), book.getName());
                    Assertions.assertEquals(BookData.BLOOD_OF_ELVES.getAuthor(), book.getAuthor());
                })
                .assertNext(book -> {
                    Assertions.assertEquals(BookData.ORIENT_EXPRESS.getId(), book.getId());
                    Assertions.assertEquals(BookData.ORIENT_EXPRESS.getName(), book.getName());
                    Assertions.assertEquals(BookData.ORIENT_EXPRESS.getAuthor(), book.getAuthor());
                })
                .expectComplete()
                .verify();
    }
}
