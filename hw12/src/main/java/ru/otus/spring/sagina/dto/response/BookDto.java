package ru.otus.spring.sagina.dto.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Set;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class BookDto {
    private String id;
    private String title;
    private String description;
    private AuthorDto author;
    private Set<GenreDto> genres;
}
