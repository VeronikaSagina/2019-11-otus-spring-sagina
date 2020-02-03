package ru.otus.spring.sagina.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateBookCommentDto {
    @NotNull
    @JsonProperty("bookId")
    public final Integer bookId;

    @NotBlank
    @JsonProperty("message")
    public final String message;

    public CreateBookCommentDto(@JsonProperty("bookId") Integer bookId,
                                @JsonProperty("message") String message) {
        this.bookId = bookId;
        this.message = message;
    }
}
