package ru.otus.spring.sagina.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.spring.sagina.TestLifecycle;
import ru.otus.spring.sagina.entity.Genre;
import ru.otus.spring.sagina.testdata.GenreData;
import ru.otus.spring.sagina.utils.TestUtils;

import java.util.Comparator;
import java.util.List;

class GenreRepositoryTest extends TestLifecycle {
    @Autowired
    private GenreRepository genreRepository;

    @Test
    void getByIdsTest() {
        List<Genre> expected = List.of(GenreData.NOVEL, GenreData.DETECTIVE, GenreData.FANTASY);
        List<Genre> actual = genreRepository.findAllByIdIn(List.of("1", "2", "3"));
        actual.sort(Comparator.comparing(Genre::getId));
        Assertions.assertTrue(TestUtils.compare(expected, actual,
                Comparator.comparing(Genre::getId).thenComparing(Genre::getName)));
    }
}
