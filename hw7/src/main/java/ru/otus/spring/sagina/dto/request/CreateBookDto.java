package ru.otus.spring.sagina.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CreateBookDto {
    @NotBlank
    @JsonProperty("title")
    public final String title;
    @NotNull
    @JsonProperty("authorId")
    public final Integer authorId;
    @NotEmpty
    @JsonProperty("genreIds")
    public final List<Integer> genreIds;

    public CreateBookDto(@JsonProperty("title") String title,
                         @JsonProperty("authorId") Integer authorId,
                         @JsonProperty("genreIds") List<Integer> genreIds) {
        this.title = title;
        this.authorId = authorId;
        this.genreIds = genreIds;
    }
}
