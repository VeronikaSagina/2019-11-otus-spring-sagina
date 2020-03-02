package ru.otus.spring.sagina.dto.response;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class BookWithCommentsDto {
    public final String id;
    public final String title;
    public final String description;
    public final AuthorDto author;
    public final Set<GenreDto> genres;
    public final List<BookCommentDto> comments;
}
