package ru.otus.spring.sagina.dto.mapper;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dto.response.GenreDto;
import ru.otus.spring.sagina.entity.Genre;

@Service
public class GenreDtoMapper {
    public GenreDto toDto(Genre genre){
        return new GenreDto(genre.getId(), genre.getName());
    }
}
