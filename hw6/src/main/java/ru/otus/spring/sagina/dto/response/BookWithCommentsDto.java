package ru.otus.spring.sagina.dto.response;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BookWithCommentsDto {
    public final int id;
    public final String title;
    public final AuthorDto author;
    public final Set<GenreDto> genres;
    public final List<BookCommentDto> comments;

    @Override
    public String toString() {
        return "(id=" + id +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", genres=" + genres + "\n" +
                ", comments= [" +
                comments.stream()
                        .map(BookCommentDto::toString)
                        .collect(Collectors.joining("\n")) +
                "])";
    }
}
