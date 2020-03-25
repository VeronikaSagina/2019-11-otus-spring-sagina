package ru.otus.spring.sagina.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class BookCommentDtoRequest {
    @JsonProperty("id")
    public final String id;
    @NotBlank
    @JsonProperty("bookId")
    public final String bookId;
    @NotBlank
    @JsonProperty("message")
    public final String message;

    public BookCommentDtoRequest(@JsonProperty("id") String id,
                                 @JsonProperty("bookId") String bookId,
                                 @JsonProperty("message") String message) {
        this.id = id;
        this.bookId = bookId;
        this.message = message;
    }
}
