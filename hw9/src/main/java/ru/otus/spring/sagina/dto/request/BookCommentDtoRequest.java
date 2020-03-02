package ru.otus.spring.sagina.dto.request;

import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class BookCommentDtoRequest {
    public final String id;
    @NotNull
    public final String bookId;
    @NotBlank
    public final String message;
}
