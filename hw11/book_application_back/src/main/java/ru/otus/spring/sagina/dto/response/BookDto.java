package ru.otus.spring.sagina.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode
public class BookDto {
    @JsonProperty("id")
    public final String id;
    @JsonProperty("name")
    public final String name;
    @JsonProperty("description")
    public final String description;
    @JsonProperty("author")
    public final AuthorDto author;
    @JsonProperty("genres")
    public final Set<GenreDto> genres;

    public BookDto(@JsonProperty("id") String id,
                   @JsonProperty("name") String name,
                   @JsonProperty("description") String description,
                   @JsonProperty("author") AuthorDto author,
                   @JsonProperty("genres") Set<GenreDto> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.author = author;
        this.genres = genres;
    }
}
