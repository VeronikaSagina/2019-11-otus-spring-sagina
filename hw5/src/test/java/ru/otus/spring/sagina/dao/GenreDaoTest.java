package ru.otus.spring.sagina.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.sagina.domain.Genre;
import ru.otus.spring.sagina.testdata.GenreData;

import java.util.List;
import java.util.Set;

@JdbcTest
@Import(GenreDaoImpl.class)
class GenreDaoTest {
    @Autowired
    private GenreDaoImpl genreDao;

    @Test
    void createTest() {
        genreDao.create(GenreData.BALLAD);
        Assertions.assertEquals(GenreData.BALLAD, genreDao.getById(5));
    }

    @Test
    void updateTest() {
        Genre genre = new Genre(1, "поэма");
        int update = genreDao.update(genre);
        Assertions.assertEquals(1, update);
        Assertions.assertEquals(genre, genreDao.getById(1));
        Assertions.assertEquals(List.of(genre), genreDao.getByType("поэма"));
    }

    @Test
    void getByIdTest() {
        Assertions.assertEquals(GenreData.DETECTIVE, genreDao.getById(2));
    }

    @Test
    void getByIdsTest() {
        Assertions.assertEquals(Set.of(GenreData.NOVEL, GenreData.DETECTIVE, GenreData.FANTASY), genreDao.getByIds(List.of(1, 2, 3)));
    }

    @Test
    void getByTypeTest() {
        Genre genre = new Genre(6, "научная фантастика");
        genreDao.create(genre);
        Assertions.assertEquals(List.of(GenreData.FANTASTIC, genre), genreDao.getByType("фантастика"));
        Assertions.assertEquals(List.of(GenreData.NOVEL), genreDao.getByType("роман"));
    }

    @Test
    void getAllTest() {
        List<Genre> genres = List.of(GenreData.DETECTIVE, GenreData.NOVEL, GenreData.FANTASTIC, GenreData.FANTASY);
        Assertions.assertEquals(genres, genreDao.getAll());
    }

    @Test
    void existsByIdTest() {
        Assertions.assertTrue(genreDao.existsById(1));
        Assertions.assertFalse(genreDao.existsById(5));
    }
}
