package ru.otus.spring.sagina.dto.mapper;

import ru.otus.spring.sagina.domain.Author;
import ru.otus.spring.sagina.dto.response.AuthorDto;

public class AuthorDtoMapper {
    public static AuthorDto getDto(Author author) {
        return new AuthorDto(author.getId(), author.getName());
    }
}
