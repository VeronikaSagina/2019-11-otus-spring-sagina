package ru.otus.spring.sagina.dto.mapper;

import ru.otus.spring.sagina.dto.response.AuthorDto;
import ru.otus.spring.sagina.entity.Author;

public class AuthorDtoMapper {
    public static AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getName());
    }
}
