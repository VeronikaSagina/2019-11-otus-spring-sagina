package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import ru.otus.spring.sagina.dto.request.CreateGenreDto;
import ru.otus.spring.sagina.dto.request.UpdateGenreDto;
import ru.otus.spring.sagina.entity.Genre;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.repository.GenreRepository;
import ru.otus.spring.sagina.testdata.GenreData;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
class GenreServiceTest {
    @Mock
    private GenreRepository genreRepository;
    @InjectMocks
    private GenreService genreService;

    @Test
    void createGenreTest() {
        Mockito.when(genreRepository.save(Mockito.any())).thenReturn(GenreData.BALLAD);
        CreateGenreDto createGenreDto = new CreateGenreDto(GenreData.BALLAD.getType());
        Genre actual = genreService.createGenre(createGenreDto);
        Mockito.verify(genreRepository).save(new Genre(null, GenreData.BALLAD.getType()));
        Assertions.assertEquals("5", actual.getId());
        Assertions.assertEquals(GenreData.BALLAD.getType(), actual.getType());
    }

    @Test
    void updateGenreTest() {
        Mockito.when(genreRepository.findById("1")).thenReturn(Optional.of(new Genre("1", GenreData.NOVEL.getType())));
        Mockito.when(genreRepository.save(Mockito.any())).thenReturn(new Genre("1", GenreData.BALLAD.getType()));
        Genre  actual = genreService.updateGenre(new UpdateGenreDto("1", GenreData.BALLAD.getType()));
        Mockito.verify(genreRepository).save(new Genre("1", GenreData.BALLAD.getType()));
        Assertions.assertEquals("1", actual.getId());
        Assertions.assertEquals(GenreData.BALLAD.getType(), actual.getType());
    }

    @Test
    void updateGenreExceptionTest() {
        Mockito.when(genreRepository.findById("1")).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class,
                () -> genreService.updateGenre(new UpdateGenreDto("1", GenreData.BALLAD.getType())));
        Mockito.verify(genreRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void getAllTest() {
        List<Genre> expected = List.of(GenreData.DETECTIVE, GenreData.FANTASTIC, GenreData.NOVEL);
        Mockito.when(genreRepository.findAll(Sort.by("type"))).thenReturn(expected);
        List<Genre> actual = genreService.getAll();
        Assertions.assertEquals(actual.stream().map(Genre::getId).collect(Collectors.toList()),
                expected.stream().map(Genre::getId).collect(Collectors.toList()));
    }

    @Test
    void getByTypeTest() {
        Mockito.when(genreRepository.findAllByTypeContainingIgnoreCaseOrderByType("ф"))
                .thenReturn(List.of(GenreData.FANTASTIC, GenreData.FANTASY));
        List<Genre> actual = genreService.getByType("ф");
        Assertions.assertEquals(List.of(GenreData.FANTASTIC, GenreData.FANTASY), actual);
    }
}
