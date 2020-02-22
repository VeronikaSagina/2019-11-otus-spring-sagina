package ru.otus.spring.sagina.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UpdateBookDto {
    @NotNull
    @JsonProperty("id")
    public final String id;
    @JsonProperty("title")
    public final String title;
    @JsonProperty("authorId")
    public final String authorId;
    @JsonProperty("genreIds")
    public final List<String> genreIds;

    public UpdateBookDto(@JsonProperty("id") String id,
                         @JsonProperty("title") String title,
                         @JsonProperty("authorId") String authorId,
                         @JsonProperty("genreIds") List<String> genreIds) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.genreIds = genreIds;
    }
}
