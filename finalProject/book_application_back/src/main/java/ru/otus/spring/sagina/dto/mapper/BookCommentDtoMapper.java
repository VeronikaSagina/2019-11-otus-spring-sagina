package ru.otus.spring.sagina.dto.mapper;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dto.response.BookCommentDto;
import ru.otus.spring.sagina.dto.response.ShortUserDto;
import ru.otus.spring.sagina.entity.BookComment;

@Service
public class BookCommentDtoMapper {
    public BookCommentDto toDto(BookComment comment) {
        return new BookCommentDto(
                comment.getId(),
                comment.getMessage(),
                new ShortUserDto(comment.getUser().getId(), comment.getUser().getLogin()));
    }
}
