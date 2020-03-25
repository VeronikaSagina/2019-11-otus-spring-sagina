package ru.otus.spring.sagina.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.sagina.dto.mapper.GenreDtoMapper;
import ru.otus.spring.sagina.dto.response.GenreDto;
import ru.otus.spring.sagina.services.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/genre")
    public List<GenreDto> getGenres(){
        return genreService.getAll().stream()
                .map(GenreDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
