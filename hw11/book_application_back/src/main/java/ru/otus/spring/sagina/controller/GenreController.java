package ru.otus.spring.sagina.controller;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.spring.sagina.dto.mapper.GenreDtoMapper;
import ru.otus.spring.sagina.dto.response.GenreDto;
import ru.otus.spring.sagina.repository.GenreRepository;

@RestController
public class GenreController {
    private final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @GetMapping("/genre")
    public Flux<GenreDto> getGenres() {
        return genreRepository.findAll(Sort.by("name"))
                .map(GenreDtoMapper::toDto);
    }
}
