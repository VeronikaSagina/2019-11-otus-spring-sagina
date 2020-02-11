package ru.otus.spring.sagina.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BookCommentDto {
    public final int id;

    public final String message;

    public final int bookId;

    @Override
    public String toString() {
        return  "(id=" + id +
                ", message='" + message + '\'' +
                ", bookId=" + bookId +
                ')';
    }
}
