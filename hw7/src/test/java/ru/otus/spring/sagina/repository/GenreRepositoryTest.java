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
import ru.otus.spring.sagina.entity.Genre;
import ru.otus.spring.sagina.testdata.GenreData;
import ru.otus.spring.sagina.utils.TestUtils;

import java.util.Comparator;
import java.util.List;

@ExtendWith(SpringExtension.class)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@DataJpaTest
class GenreRepositoryTest {
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void getByIdsTest() {
        Statistics statistics = getSessionStatistics();

        List<Genre> expected = List.of(GenreData.NOVEL, GenreData.DETECTIVE, GenreData.FANTASY);
        List<Genre> actual = genreRepository.findAllByIdIn(List.of(1, 2, 3));
        actual.sort(Comparator.comparing(Genre::getId));
        Assertions.assertTrue(TestUtils.compare(expected, actual,
                Comparator.comparing(Genre::getId).thenComparing(Genre::getType)));

        Assertions.assertEquals(1, statistics.getPrepareStatementCount());
    }

    @Test
    void getByTypeTest() {
        Genre forSave = new Genre();
        forSave.setType("научная фантастика");
        Genre genre = genreRepository.save(forSave);
        Assertions.assertTrue(TestUtils.compare(List.of(genre, GenreData.FANTASTIC),
                genreRepository.findAllByTypeContainingIgnoreCaseOrderByType("фантастика"),
                Comparator.comparing(Genre::getId).thenComparing(Genre::getType)));
        Assertions.assertTrue(TestUtils.compare(List.of(GenreData.NOVEL),
                genreRepository.findAllByTypeContainingIgnoreCaseOrderByType("роман"),
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
