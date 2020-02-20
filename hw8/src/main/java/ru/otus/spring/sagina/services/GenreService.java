package ru.otus.spring.sagina.services;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dto.request.CreateGenreDto;
import ru.otus.spring.sagina.dto.request.UpdateGenreDto;
import ru.otus.spring.sagina.entity.Genre;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.repository.GenreRepository;

import java.util.List;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Genre createGenre(CreateGenreDto genreDto) {
        Genre created = new Genre();
        created.setType(genreDto.type);
        return genreRepository.save(created);
    }

    public Genre updateGenre(UpdateGenreDto genreDto) {
        return genreRepository.findById(genreDto.id).map(it -> {
            it.setType(genreDto.type);
            return genreRepository.save(it);
        }).orElseThrow(() -> new NotFoundException(String.format("не найден жанр с id=%s", genreDto.id)));
    }

    public List<Genre> getAll() {
        return genreRepository.findAll(Sort.by("type"));
    }

    public List<Genre> getByType(String type) {
        return genreRepository.findAllByTypeContainingIgnoreCaseOrderByType(type);
    }

    public void deleteGenre(String id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("не найден жанр с id=%s", id)));
        genreRepository.delete(genre);
    }
}
