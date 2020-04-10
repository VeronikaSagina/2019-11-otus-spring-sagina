package ru.otus.spring.sagina.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.sagina.dto.mapper.GenreDtoMapper;
import ru.otus.spring.sagina.dto.response.GenreDto;
import ru.otus.spring.sagina.services.GenreService;

import java.util.List;
import java.util.stream.Collectors;
@Api(tags = "GenreController")
@RestController
public class GenreController {
    private final GenreService genreService;
    private final GenreDtoMapper genreDtoMapper;

    public GenreController(GenreService genreService,
                           GenreDtoMapper genreDtoMapper) {
        this.genreService = genreService;
        this.genreDtoMapper = genreDtoMapper;
    }

    @GetMapping("/genre")
    @ApiOperation("получение списка жанров")
    public List<GenreDto> getGenres(){
        return genreService.getAll().stream()
                .map(genreDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
