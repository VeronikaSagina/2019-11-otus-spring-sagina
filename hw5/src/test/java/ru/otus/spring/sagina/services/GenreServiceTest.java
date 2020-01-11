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
import java.util.stream.Collectors;

@SpringBootTest
class GenreServiceTest {
    @Mock
    private GenreDao genreDao;
    @InjectMocks
    private GenreService genreService;

    @Test
    void createGenreTest() {
        Mockito.when(genreDao.getIdFromSequence()).thenReturn(5);
        CreateGenreDto createGenreDto = new CreateGenreDto(GenreData.BALLAD.getType());
        GenreDto actual = genreService.createGenre(createGenreDto);
        Mockito.verify(genreDao).create(Mockito.any());
        Assertions.assertEquals(5, actual.id);
        Assertions.assertEquals(GenreData.BALLAD.getType(), actual.type);
    }

    @Test
    void updateGenreTest() {
        Mockito.when(genreDao.update(Mockito.any())).thenReturn(1);
        GenreDto actual = genreService.updateGenre(new UpdateGenreDto(1, GenreData.BALLAD.getType()));
        Assertions.assertEquals(1, actual.id);
        Assertions.assertEquals(GenreData.BALLAD.getType(), actual.type);
    }

    @Test
    void updateGenreExceptionTest() {
        Assertions.assertThrows(NotFoundException.class,
                () -> genreService.updateGenre(new UpdateGenreDto(1, GenreData.BALLAD.getType())));
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
