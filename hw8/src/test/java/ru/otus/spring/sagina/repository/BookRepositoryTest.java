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
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.entity.BookComment;
import ru.otus.spring.sagina.entity.Genre;
import ru.otus.spring.sagina.events.DeleteCascadeEvents;
import ru.otus.spring.sagina.testdata.AuthorData;
import ru.otus.spring.sagina.testdata.BookData;
import ru.otus.spring.sagina.testdata.GenreData;
import ru.otus.spring.sagina.testdata.InitDb;
import ru.otus.spring.sagina.utils.TestUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ExtendWith(SpringExtension.class)
@Import({InitDb.class, DeleteCascadeEvents.class})
@DataMongoTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookCommentRepository commentRepository;
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
    void getByAuthorIdTest() {
        List<Book> actual = bookRepository.findAllByAuthorIdIn(List.of("3"));
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(BookData.ORIENT_EXPRESS.getAuthor().getId(), actual.get(0).getAuthor().getId());
        Assertions.assertEquals(BookData.ORIENT_EXPRESS.getTitle(), actual.get(0).getTitle());
        Assertions.assertEquals(BookData.ORIENT_EXPRESS.getId(), actual.get(0).getId());
        Assertions.assertEquals(List.of(), bookRepository.findAllByAuthorIdIn(List.of("6")));
    }

    @Test
    void getByTitleTest() {
        List<Book> actual = bookRepository.findAllByTitleContainingIgnoreCaseOrderByTitle("эльф");
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(BookData.BLOOD_OF_ELVES.getId(), actual.get(0).getId());
        Assertions.assertEquals(BookData.BLOOD_OF_ELVES.getAuthor().getId(), actual.get(0).getAuthor().getId());
        Assertions.assertEquals(BookData.BLOOD_OF_ELVES.getTitle(), actual.get(0).getTitle());
    }

    @Test
    public void deleteAllByAuthorIdTest() {
        String authorId = AuthorData.TOLSTOY.getId();
        List<Book> booksForDelete = bookRepository.findAllByAuthorIdIn(List.of(authorId));
        Assertions.assertFalse(booksForDelete.isEmpty());
        List<BookComment> commentsForDelete = new ArrayList<>();
        booksForDelete.forEach(it -> commentsForDelete.addAll(commentRepository.findAllByBookId(it.getId())));
        Assertions.assertFalse(commentsForDelete.isEmpty());
        bookRepository.deleteAll(booksForDelete);
        Assertions.assertTrue(bookRepository.findAllByAuthorIdIn(List.of(authorId)).isEmpty());
        List<BookComment> actualComments = new ArrayList<>();
        booksForDelete.forEach(it -> actualComments.addAll(commentRepository.findAllByBookId(it.getId())));
        Assertions.assertTrue(actualComments.isEmpty());
    }

    @Test
    public void findFirstByGenreIdTest() {
        Book actual = bookRepository.findFirstByGenreId("1");
        Assertions.assertEquals(BookData.ANNA_KARENINA.getId(), actual.getId());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getTitle(), actual.getTitle());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getId(), actual.getAuthor().getId());
    }

    @Test
    public void findAllByGenreIdTest() {
        List<Book> actual = bookRepository.findAllByGenreId("3");
        Assertions.assertTrue(TestUtils.compare(List.of(BookData.BLOOD_OF_ELVES), actual,
                Comparator.comparing(Book::getId).thenComparing(Book::getTitle)
                        .thenComparing(b -> b.getAuthor().getId())));
        Assertions.assertTrue(TestUtils.compare(List.of(GenreData.FANTASY), actual.get(0).getGenres(),
                Comparator.comparing(Genre::getId).thenComparing(Genre::getType)));
    }
}
