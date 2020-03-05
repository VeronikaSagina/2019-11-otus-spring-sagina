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
import ru.otus.spring.sagina.entity.Genre;
import ru.otus.spring.sagina.testdata.GenreData;
import ru.otus.spring.sagina.testdata.InitDb;
import ru.otus.spring.sagina.utils.TestUtils;

import java.util.Comparator;
import java.util.List;

@ExtendWith(SpringExtension.class)
@Import(InitDb.class)
@DataMongoTest
class GenreRepositoryTest {
    @Autowired
    private GenreRepository genreRepository;
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
    void getByIdsTest() {
        List<Genre> expected = List.of(GenreData.NOVEL, GenreData.DETECTIVE, GenreData.FANTASY);
        List<Genre> actual = genreRepository.findAllByIdIn(List.of("1", "2", "3"));
        actual.sort(Comparator.comparing(Genre::getId));
        Assertions.assertTrue(TestUtils.compare(expected, actual,
                Comparator.comparing(Genre::getId).thenComparing(Genre::getType)));
    }
}
