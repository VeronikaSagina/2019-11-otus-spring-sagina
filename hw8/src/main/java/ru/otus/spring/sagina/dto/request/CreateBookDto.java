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
    public final String authorId;
    @NotEmpty
    @JsonProperty("genreIds")
    public final List<String> genreIds;

    public CreateBookDto(@JsonProperty("title") String title,
                         @JsonProperty("authorId") String authorId,
                         @JsonProperty("genreIds") List<String> genreIds) {
        this.title = title;
        this.authorId = authorId;
        this.genreIds = genreIds;
    }
}
