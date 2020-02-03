package ru.otus.spring.sagina.dto.mapper;

import ru.otus.spring.sagina.domain.BookComment;
import ru.otus.spring.sagina.dto.response.BookCommentDto;

public class BookCommentDtoMapper {
    public static BookCommentDto toDto(BookComment comment){
        return new BookCommentDto(comment.getId(), comment.getMessage(), comment.getBook().getId());
    }
}
