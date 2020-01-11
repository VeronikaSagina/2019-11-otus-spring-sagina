package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.sagina.dao.GenreDao;
import ru.otus.spring.sagina.domain.Genre;
import ru.otus.spring.sagina.dto.mapper.GenreDtoMapper;
import ru.otus.spring.sagina.dto.request.CreateGenreDto;
import ru.otus.spring.sagina.dto.request.UpdateGenreDto;
import ru.otus.spring.sagina.dto.response.GenreDto;
import ru.otus.spring.sagina.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {
    private final GenreDao genreDao;

    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Transactional
    public GenreDto createGenre(CreateGenreDto genreDto) {
        int id = genreDao.getIdFromSequence();
        Genre created = new Genre(id, genreDto.type);
        genreDao.create(created);
        return GenreDtoMapper.toDto(created);
    }

    @Transactional
    public GenreDto updateGenre(UpdateGenreDto genreDto) {
        Genre updated = new Genre(genreDto.id, genreDto.type);
        int updatedRow = genreDao.update(updated);
        if (updatedRow == 0) {
            throw new NotFoundException(String.format("не найден жанр с id=%s", genreDto.id));
        }
        return GenreDtoMapper.toDto(updated);
    }

    public List<GenreDto> getAll() {
        return genreDao.getAll().stream()
                .map(GenreDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<GenreDto> getByType(String type) {
        return genreDao.getByType(type).stream()
                .map(GenreDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
