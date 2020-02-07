package ru.otus.spring.sagina.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.spring.sagina.domain.Genre;
import ru.otus.spring.sagina.testdata.GenreData;
import ru.otus.spring.sagina.utils.TestUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@DataJpaTest
@Import(GenreDaoImpl.class)
class GenreDaoTest {
    @Autowired
    private GenreDaoImpl genreDao;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void createTest() {
        Genre expected = genreDao.save(new Genre(GenreData.BALLAD.getType()));
        Optional<Genre> actual = genreDao.findById(5);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected.getId(), actual.get().getId());
        Assertions.assertEquals(expected.getType(), actual.get().getType());
    }

    @Test
    void updateTest() {
        Genre genre = new Genre(1, "поэма");
        Assertions.assertNotEquals(Optional.of(genre), genreDao.findById(1));
        Genre saved = genreDao.save(genre);
        entityManager.flush();
        entityManager.detach(saved);
        Assertions.assertEquals(Optional.of(saved), genreDao.findById(1));
        Assertions.assertEquals(List.of(saved), genreDao.getByType("поэма"));
    }

    @Test
    void getByIdTest() {
        Optional<Genre> actual = genreDao.findById(2);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(GenreData.DETECTIVE.getId(), actual.get().getId());
        Assertions.assertEquals(GenreData.DETECTIVE.getType(), actual.get().getType());
    }

    @Test
    void getByIdsTest() {
        List<Genre> expected = List.of(GenreData.NOVEL, GenreData.DETECTIVE, GenreData.FANTASY);
        List<Genre> actual = genreDao.getByIds(List.of(1, 2, 3));
        Assertions.assertTrue(TestUtils.compare(expected, actual,
                Comparator.comparing(Genre::getId).thenComparing(Genre::getType)));
    }

    @Test
    void getByTypeTest() {
        Genre genre = genreDao.save(new Genre("научная фантастика"));
        Assertions.assertTrue(TestUtils.compare(List.of(GenreData.FANTASTIC, genre), genreDao.getByType("фантастика"),
                Comparator.comparing(Genre::getId).thenComparing(Genre::getType)));
        Assertions.assertTrue(TestUtils.compare(List.of(GenreData.NOVEL), genreDao.getByType("роман"),
                Comparator.comparing(Genre::getId).thenComparing(Genre::getType)));
    }

    @Test
    void getAllTest() {
        List<Genre> expected = List.of(GenreData.DETECTIVE, GenreData.NOVEL, GenreData.FANTASTIC, GenreData.FANTASY);
        List<Genre> actual = genreDao.getAll();
        Assertions.assertTrue(TestUtils.compare(expected, actual,
                Comparator.comparing(Genre::getId).thenComparing(Genre::getType)));
    }
}
