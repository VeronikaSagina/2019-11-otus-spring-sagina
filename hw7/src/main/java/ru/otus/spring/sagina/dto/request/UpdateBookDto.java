package ru.otus.spring.sagina.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UpdateBookDto {
    @NotNull
    @JsonProperty("id")
    public final Integer id;
    @JsonProperty("title")
    public final String title;
    @JsonProperty("authorId")
    public final Integer authorId;
    @JsonProperty("genreIds")
    public final List<Integer> genreIds;

    public UpdateBookDto(@JsonProperty("id") Integer id,
                         @JsonProperty("title") String title,
                         @JsonProperty("authorId") Integer authorId,
                         @JsonProperty("genreIds") List<Integer> genreIds) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.genreIds = genreIds;
    }
}
