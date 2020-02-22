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
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.entity.BookComment;
import ru.otus.spring.sagina.events.DeleteCascadeEvents;
import ru.otus.spring.sagina.testdata.AuthorData;
import ru.otus.spring.sagina.testdata.InitDb;
import ru.otus.spring.sagina.utils.TestUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@Import({InitDb.class, DeleteCascadeEvents.class})
@DataMongoTest
class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;
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
    void deleteTest() {
        Optional<Author> forDelete = authorRepository.findById(AuthorData.TOLSTOY.getId());
        Assertions.assertTrue(forDelete.isPresent());
        List<Book> booksForDelete = bookRepository.findAllByAuthorIdIn(List.of(forDelete.get().getId()));
        Assertions.assertFalse(booksForDelete.isEmpty());
        List<BookComment> commentsForDelete = new ArrayList<>();
        booksForDelete.forEach(it -> commentsForDelete.addAll(commentRepository.findAllByBookId(it.getId())));
        Assertions.assertFalse(commentsForDelete.isEmpty());
        authorRepository.delete(forDelete.get());
        Assertions.assertTrue(authorRepository.findById(AuthorData.TOLSTOY.getId()).isEmpty());
        Assertions.assertTrue(bookRepository.findAllByAuthorIdIn(List.of(forDelete.get().getId())).isEmpty());
        List<BookComment> actualComments = new ArrayList<>();
        booksForDelete.forEach(it -> actualComments.addAll(commentRepository.findAllByBookId(it.getId())));
        Assertions.assertTrue(actualComments.isEmpty());
    }

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
