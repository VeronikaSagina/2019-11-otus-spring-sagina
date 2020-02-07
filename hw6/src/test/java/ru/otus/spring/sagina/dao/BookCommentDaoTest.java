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
import ru.otus.spring.sagina.domain.BookComment;
import ru.otus.spring.sagina.testdata.BookCommentData;
import ru.otus.spring.sagina.testdata.BookData;
import ru.otus.spring.sagina.utils.TestUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@DataJpaTest
@Import(BookCommentDaoImpl.class)
class BookCommentDaoTest {
    @Autowired
    private BookCommentDaoImpl commentDao;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void save() {
        BookComment bookComment = new BookComment("еще один комментарий", BookData.ANNA_KARENINA);
        BookComment saved = commentDao.save(bookComment);
        List<BookComment> expected =
                List.of(BookCommentData.KARENINA_COMMENT_1, BookCommentData.KARENINA_COMMENT_2, bookComment);
        List<BookComment> actual = commentDao.getAllByBookId(1);
        Assertions.assertTrue(commentDao.findById(saved.getId()).isPresent());
        Assertions.assertTrue(TestUtils.compare(expected, actual,
                Comparator.comparing(BookComment::getId).thenComparing(BookComment::getMessage)
                        .thenComparing(c -> c.getBook().getId())));
    }

    @Test
    void findById() {
        Statistics statistics = getSessionStatistics();
        Optional<BookComment> actual = commentDao.findById(1);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(BookCommentData.KARENINA_COMMENT_1.getId(), actual.get().getId());
        Assertions.assertEquals(BookCommentData.KARENINA_COMMENT_1.getMessage(), actual.get().getMessage());
        Assertions.assertEquals(BookCommentData.KARENINA_COMMENT_1.getBook().getId(), actual.get().getBook().getId());
        Assertions.assertEquals(1, statistics.getPrepareStatementCount());
    }

    @Test
    void getAllByBookId() {
        Statistics statistics = getSessionStatistics();
        List<BookComment> expected = List.of(BookCommentData.KARENINA_COMMENT_1, BookCommentData.KARENINA_COMMENT_2);
        List<BookComment> actual = commentDao.getAllByBookId(1);
        Assertions.assertTrue(TestUtils.compare(expected, actual,
                Comparator.comparing(BookComment::getId).thenComparing(BookComment::getMessage)
                        .thenComparing(c -> c.getBook().getId())));
        Assertions.assertEquals(1, statistics.getPrepareStatementCount());
    }

    @Test
    void delete() {
        Statistics statistics = getSessionStatistics();

        List<BookComment> expected = List.of(BookCommentData.KARENINA_COMMENT_2);
        Optional<BookComment> forDelete = commentDao.findById(1);
        Assertions.assertTrue(forDelete.isPresent());
        commentDao.delete(forDelete.get());
        entityManager.flush();
        entityManager.detach(forDelete.get());
        Assertions.assertTrue(TestUtils.compare(expected, commentDao.getAllByBookId(1),
                Comparator.comparing(BookComment::getId).thenComparing(BookComment::getMessage)
                        .thenComparing(c -> c.getBook().getId())));

        Assertions.assertEquals(3, statistics.getPrepareStatementCount());
    }

    @Test
    void deleteAllByBookId() {
        Statistics statistics = getSessionStatistics();
        List<BookComment> forDelete = commentDao.getAllByBookId(1);
        commentDao.deleteAllByBookId(1);
        entityManager.flush();
        forDelete.forEach(entityManager::detach);
        Assertions.assertTrue(commentDao.getAllByBookId(1).isEmpty());

        Assertions.assertEquals(3, statistics.getPrepareStatementCount());
    }

    @Test
    void deleteById() {
        Statistics statistics = getSessionStatistics();

        List<BookComment> expected = List.of(BookCommentData.KARENINA_COMMENT_2);
        Optional<BookComment> forDelete = commentDao.findById(1);
        Assertions.assertTrue(forDelete.isPresent());
        commentDao.deleteById(1);
        entityManager.flush();
        entityManager.detach(forDelete.get());
        Assertions.assertTrue(TestUtils.compare(expected, commentDao.getAllByBookId(1),
                Comparator.comparing(BookComment::getId).thenComparing(BookComment::getMessage)
                        .thenComparing(c -> c.getBook().getId())));

        Assertions.assertEquals(3, statistics.getPrepareStatementCount());
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