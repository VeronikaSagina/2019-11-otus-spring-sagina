package ru.otus.spring.sagina.dto.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class BookWithCommentsDto {
    private String id;
    private String title;
    private String description;
    private AuthorDto author;
    private Set<GenreDto> genres;
    private List<BookCommentDto> comments;
}
