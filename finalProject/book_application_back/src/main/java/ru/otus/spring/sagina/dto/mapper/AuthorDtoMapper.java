package ru.otus.spring.sagina.dto.mapper;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dto.response.AuthorDto;
import ru.otus.spring.sagina.entity.Author;

@Service
public class AuthorDtoMapper {
    public AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getName(), author.getDescription());
    }
}
