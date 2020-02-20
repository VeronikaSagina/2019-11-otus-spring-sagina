package ru.otus.spring.sagina.dto.mapper;

import ru.otus.spring.sagina.dto.response.BookWithCommentsDto;
import ru.otus.spring.sagina.entity.Book;

import java.util.stream.Collectors;

public class BookWithCommentsDtoMapper {
    public static BookWithCommentsDto toDto(Book book) {
        return new BookWithCommentsDto(book.getId(),
                book.getTitle(),
                AuthorDtoMapper.toDto(book.getAuthor()),
                book.getGenres()
                        .stream()
                        .map(GenreDtoMapper::toDto)
                        .collect(Collectors.toSet()),
                book.getComments()
                        .stream()
                        .map(BookCommentDtoMapper::toDto)
                        .collect(Collectors.toList()));
    }
}
