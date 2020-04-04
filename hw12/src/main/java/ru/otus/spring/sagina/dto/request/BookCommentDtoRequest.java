package ru.otus.spring.sagina.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class BookCommentDtoRequest {
    private String id;
    @NotNull
    private String bookId;
    @NotBlank
    private String message;
}
