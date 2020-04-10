package ru.otus.spring.sagina.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.sagina.dto.mapper.AuthorDtoMapper;
import ru.otus.spring.sagina.dto.response.AuthorDto;
import ru.otus.spring.sagina.services.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "AuthorController")
@RestController
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorDtoMapper authorDtoMapper;

    public AuthorController(AuthorService authorService,
                            AuthorDtoMapper authorDtoMapper) {
        this.authorService = authorService;
        this.authorDtoMapper = authorDtoMapper;
    }

    @GetMapping("/author")
    @ApiOperation("получение списка авторов")
    public List<AuthorDto> getAuthors(){
        return authorService.getAll().stream()
                .map(authorDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
