package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import ru.otus.spring.sagina.entity.Genre;
import ru.otus.spring.sagina.repository.GenreRepository;
import ru.otus.spring.sagina.testdata.GenreData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GenreServiceTest {
    @Mock
    private GenreRepository genreRepository;
    @InjectMocks
    private GenreService genreService;

    @Test
    void getAllTest() {
        List<Genre> expected = List.of(GenreData.DETECTIVE, GenreData.FANTASTIC, GenreData.NOVEL);
        Mockito.when(genreRepository.findAll(Sort.by("name"))).thenReturn(expected);
        List<Genre> actual = genreService.getAll();
        assertEquals(expected, actual);
    }
}
