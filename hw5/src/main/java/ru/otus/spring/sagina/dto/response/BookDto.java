package ru.otus.spring.sagina.dto.response;

import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class BookDto {
    public final int id;
    public final String title;
    public final AuthorDto author;
    public final Set<GenreDto> genres;

    @Override
    public String toString() {
        return "(id=" + id +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", genres=" + genres +
                ')';
    }
}
