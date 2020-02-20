package ru.otus.spring.sagina.dto.mapper;

import ru.otus.spring.sagina.dto.response.BookCommentDto;
import ru.otus.spring.sagina.entity.BookComment;

public class BookCommentDtoMapper {
    public static BookCommentDto toDto(BookComment comment){
        return new BookCommentDto(comment.getId(), comment.getMessage(), comment.getBook().getId());
    }
}
