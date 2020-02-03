package ru.otus.spring.sagina.dto.mapper;

import ru.otus.spring.sagina.domain.Genre;
import ru.otus.spring.sagina.dto.response.GenreDto;

public class GenreDtoMapper {
    public static GenreDto toDto(Genre genre){
        return new GenreDto(genre.getId(), genre.getType());
    }
}
