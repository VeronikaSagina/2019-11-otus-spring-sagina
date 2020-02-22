package ru.otus.spring.sagina.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BookCommentDto {
    public final String id;

    public final String message;

    public final String bookId;

    @Override
    public String toString() {
        return  "(id=" + id
                + ", message='" + message + '\''
                + ", bookId=" + bookId
                + ')';
    }
}
