package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.sagina.dao.GenreDao;
import ru.otus.spring.sagina.domain.Genre;
import ru.otus.spring.sagina.dto.mapper.GenreDtoMapper;
import ru.otus.spring.sagina.dto.request.CreateGenreDto;
import ru.otus.spring.sagina.dto.request.UpdateGenreDto;
import ru.otus.spring.sagina.dto.response.GenreDto;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.testdata.GenreData;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
class GenreServiceTest {
    @Mock
    private GenreDao genreDao;
    @InjectMocks
    private GenreService genreService;

    @Test
    void createGenreTest() {
        Mockito.when(genreDao.save(Mockito.any())).thenReturn(GenreData.BALLAD);
        CreateGenreDto createGenreDto = new CreateGenreDto(GenreData.BALLAD.getType());
        GenreDto actual = genreService.createGenre(createGenreDto);
        Mockito.verify(genreDao).save(new Genre(GenreData.BALLAD.getType()));
        Assertions.assertEquals(5, actual.id);
        Assertions.assertEquals(GenreData.BALLAD.getType(), actual.type);
    }

    @Test
    void updateGenreTest() {
        Mockito.when(genreDao.findById(1)).thenReturn(Optional.of(GenreData.NOVEL));
        Mockito.when(genreDao.save(Mockito.any())).thenReturn(new Genre(1, GenreData.BALLAD.getType()));
        GenreDto actual = genreService.updateGenre(new UpdateGenreDto(1, GenreData.BALLAD.getType()));
        Mockito.verify(genreDao).save(new Genre(1, GenreData.BALLAD.getType()));
        Assertions.assertEquals(1, actual.id);
        Assertions.assertEquals(GenreData.BALLAD.getType(), actual.type);
    }

    @Test
    void updateGenreExceptionTest() {
        Mockito.when(genreDao.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class,
                () -> genreService.updateGenre(new UpdateGenreDto(1, GenreData.BALLAD.getType())));
        Mockito.verify(genreDao, Mockito.never()).save(Mockito.any());
    }

    @Test
    void getAllTest() {
        List<Genre> expected = List.of(GenreData.DETECTIVE, GenreData.FANTASTIC, GenreData.NOVEL);
        Mockito.when(genreDao.getAll()).thenReturn(expected);
        List<GenreDto> actual = genreService.getAll();
        Assertions.assertEquals(actual.stream().map(g -> g.id).collect(Collectors.toList()),
                expected.stream().map(Genre::getId).collect(Collectors.toList()));
    }

    @Test
    void getByTypeTest() {
        Mockito.when(genreDao.getByType("ф")).thenReturn(List.of(GenreData.FANTASTIC, GenreData.FANTASY));
        List<GenreDto> actual = genreService.getByType("ф");
        Assertions.assertEquals(
                List.of(GenreDtoMapper.toDto(GenreData.FANTASTIC), GenreDtoMapper.toDto(GenreData.FANTASY)),
                actual);
    }
}
