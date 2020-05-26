package ru.otus.spring.sagina.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.otus.spring.sagina.dto.response.AuthorDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class BookDtoRequest {
    @JsonProperty("id")
    public final String id;
    @NotBlank
    @JsonProperty("name")
    public final String name;
    @NotBlank
    @JsonProperty("description")
    public final String description;
    @NotNull
    @JsonProperty("author")
    public final AuthorDto author;
    @NotEmpty
    @JsonProperty("genres")
    public final List<String> genreIds;

    public BookDtoRequest(@JsonProperty("id") String id,
                          @JsonProperty("name") String name,
                          @JsonProperty("description") String description,
                          @JsonProperty("author") AuthorDto author,
                          @JsonProperty("genres") List<String> genreIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.author = author;
        this.genreIds = genreIds;
    }
}
