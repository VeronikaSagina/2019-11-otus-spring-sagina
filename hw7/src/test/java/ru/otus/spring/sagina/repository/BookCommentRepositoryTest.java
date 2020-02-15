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
import ru.otus.spring.sagina.entity.BookComment;
import ru.otus.spring.sagina.testdata.BookCommentData;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@DataJpaTest
class BookCommentRepositoryTest {
    @Autowired
    private BookCommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findById() {
        Statistics statistics = getSessionStatistics();
        Optional<BookComment> actual = commentRepository.findById(1);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(BookCommentData.KARENINA_COMMENT_1.getId(), actual.get().getId());
        Assertions.assertEquals(BookCommentData.KARENINA_COMMENT_1.getMessage(), actual.get().getMessage());
        Assertions.assertEquals(BookCommentData.KARENINA_COMMENT_1.getBook().getId(), actual.get().getBook().getId());
        Assertions.assertEquals(1, statistics.getPrepareStatementCount());
    }

    @Test
    void deleteAllByBookId() {
        Statistics statistics = getSessionStatistics();
        List<BookComment> forDelete = bookRepository.getOne(1).getComments();
        commentRepository.deleteAllByBookIdIn(List.of(1));
        entityManager.flush();
        forDelete.forEach(entityManager::detach);
        Assertions.assertTrue(bookRepository.getOne(1).getComments().isEmpty());

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