package ru.otus.spring.sagina.dto.response;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BookWithCommentsDto {
    public final String id;
    public final String title;
    public final AuthorDto author;
    public final Set<GenreDto> genres;
    public final List<BookCommentDto> comments;

    @Override
    public String toString() {
        String com = comments.isEmpty() ? "[]" : "\n" + comments.stream()
                .map(BookCommentDto::toString)
                .collect(Collectors.joining("\n"));
        return String.format("(id=%s, title='%s', author=%s, genres=[%s],\ncomments: %s)",
                id, title, author, genres, com);

    }
}
