package ru.otus.spring.sagina.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.spring.sagina.entity.Genre;
import ru.otus.spring.sagina.testdata.GenreData;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class GenreRepositoryTest {
    @Autowired
    private GenreRepository genreRepository;

    @Test
    void getByIdsTest() {
        List<Genre> expected = List.of(GenreData.NOVEL, GenreData.DETECTIVE, GenreData.FANTASY);
        List<Genre> actual = genreRepository.findAllByIdIn(List.of(1L, 2L, 3L));
        actual.sort(Comparator.comparing(Genre::getId));

        assertEquals(expected, actual);
    }
}
