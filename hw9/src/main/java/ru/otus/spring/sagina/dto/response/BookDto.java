package ru.otus.spring.sagina.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class BookDto {
    public final String id;
    public final String title;
    public final String description;
    public final AuthorDto author;
    public final Set<GenreDto> genres;
}
