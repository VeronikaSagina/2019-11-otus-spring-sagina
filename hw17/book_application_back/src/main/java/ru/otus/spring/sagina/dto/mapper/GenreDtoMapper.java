package ru.otus.spring.sagina.dto.mapper;

import ru.otus.spring.sagina.dto.response.GenreDto;
import ru.otus.spring.sagina.entity.Genre;

public class GenreDtoMapper {
    public static GenreDto toDto(Genre genre){
        return new GenreDto(genre.getId(), genre.getName());
    }
}
