package ru.otus.spring.sagina.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.sagina.dto.mapper.GenreDtoMapper;
import ru.otus.spring.sagina.dto.request.CreateGenreDto;
import ru.otus.spring.sagina.dto.request.UpdateGenreDto;
import ru.otus.spring.sagina.dto.response.GenreDto;
import ru.otus.spring.sagina.services.GenreService;

import java.util.List;
import java.util.UUID;
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
    @ApiOperation("получить список жанров")
    public List<GenreDto> getGenres(){
        return genreService.getAll().stream()
                .map(genreDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/genre")
    @ApiOperation("добавить жанр")
    public GenreDto create(@RequestBody CreateGenreDto createGenreDto) {
        return genreDtoMapper.toDto(genreService.create(createGenreDto));
    }

    @PatchMapping("/genre")
    @ApiOperation("редактировать жанр")
    public GenreDto update(@RequestBody UpdateGenreDto updateGenreDto) {
        return genreDtoMapper.toDto(genreService.update(updateGenreDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/genre/{id}")
    @ApiOperation("удалить жанр")
    public void delete(@PathVariable("id") UUID id) {
        genreService.delete(id);
    }
}
