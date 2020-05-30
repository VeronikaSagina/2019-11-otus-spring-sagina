package ru.otus.spring.sagina.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.sagina.dto.mapper.AuthorDtoMapper;
import ru.otus.spring.sagina.dto.request.CreateAuthorDto;
import ru.otus.spring.sagina.dto.request.UpdateAuthorDto;
import ru.otus.spring.sagina.dto.response.AuthorDto;
import ru.otus.spring.sagina.services.AuthorService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Api(tags = "AuthorController")
@RestController
public class AuthorController {
    private final static Logger LOGGER = LoggerFactory.getLogger(AuthorController.class);
    private final AuthorService authorService;
    private final AuthorDtoMapper authorDtoMapper;

    public AuthorController(AuthorService authorService,
                            AuthorDtoMapper authorDtoMapper) {
        this.authorService = authorService;
        this.authorDtoMapper = authorDtoMapper;
    }

    @GetMapping("/author")
    @ApiOperation("получить список авторов")
    public List<AuthorDto> getAuthors(@RequestParam(value = "name", required = false) String name) {
        LOGGER.debug("getting all authors");
        return authorService.getAll(name).stream()
                .map(authorDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/author/{id}")
    @ApiOperation("получить автора по id")
    public AuthorDto getById(@PathVariable("id") UUID id) {
        LOGGER.debug("getting author by id [{}]", id);
        return authorDtoMapper.toDto(authorService.getById(id));
    }


    @PostMapping("/author")
    @ApiOperation("добавить автора")
    public AuthorDto create(@RequestBody CreateAuthorDto createAuthorDto) {
        LOGGER.debug("creating author [{}]", createAuthorDto.getName());
        return authorDtoMapper.toDto(authorService.create(createAuthorDto));
    }

    @PatchMapping("/author")
    @ApiOperation("редактировать автора")
    public AuthorDto update(@RequestBody UpdateAuthorDto updateAuthorDto) {
        LOGGER.debug("updating author [{}, {}]",updateAuthorDto.getId(), updateAuthorDto.getName());
        return authorDtoMapper.toDto(authorService.update(updateAuthorDto));
    }

    @DeleteMapping("/author/{id}")
    @ApiOperation("удалить автора по id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable("id") UUID id){
        LOGGER.debug("deleting author with id: [{}]", id);
        authorService.delete(id);
    }
}
