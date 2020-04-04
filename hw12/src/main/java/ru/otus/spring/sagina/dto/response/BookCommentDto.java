package ru.otus.spring.sagina.dto.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class BookCommentDto {
    private String id;
    private String message;
    private String bookId;
}
