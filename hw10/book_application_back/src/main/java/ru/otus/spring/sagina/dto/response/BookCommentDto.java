package ru.otus.spring.sagina.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookCommentDto {
    @JsonProperty("id")
    public final String id;
    @JsonProperty("message")
    public final String message;
    @JsonProperty("bookId")
    public final String bookId;

    public BookCommentDto(@JsonProperty("id") String id,
                          @JsonProperty("message") String message,
                          @JsonProperty("bookId") String bookId) {
        this.id = id;
        this.message = message;
        this.bookId = bookId;
    }
}
