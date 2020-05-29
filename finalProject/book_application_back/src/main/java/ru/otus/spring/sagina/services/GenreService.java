package ru.otus.spring.sagina.services;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dto.request.CreateGenreDto;
import ru.otus.spring.sagina.dto.request.UpdateGenreDto;
import ru.otus.spring.sagina.entity.Genre;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.repository.GenreRepository;

import java.util.List;
import java.util.UUID;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getAll() {
        return genreRepository.findAll(Sort.by("name"));
    }

    public Genre create(CreateGenreDto createGenreDto) {
        Genre genre = new Genre();
        genre.setName(createGenreDto.getName());
        return genreRepository.save(genre);
    }

    public Genre update(UpdateGenreDto updateGenreDto) {
        return genreRepository.findById(updateGenreDto.getId())
                .map(it -> {
                    it.setName(updateGenreDto.getName());
                    return genreRepository.save(it);
                })
                .orElseThrow(() -> {
                    throw new NotFoundException("Не найден жанр с id: " + updateGenreDto.getId());
                });
    }

    public void delete(UUID id) {
        genreRepository.findById(id)
                .ifPresentOrElse(genreRepository::delete, () -> {
                    throw new NotFoundException("Не найден жанр с id: " + id);
                });
    }
}
