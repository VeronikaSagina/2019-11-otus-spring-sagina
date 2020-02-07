package ru.otus.spring.sagina.dto.mapper;

import ru.otus.spring.sagina.domain.Book;
import ru.otus.spring.sagina.dto.response.BookWithCommentsDto;

import java.util.stream.Collectors;

public class BookWithCommentsDtoMapper {
    public static BookWithCommentsDto toDto(Book book) {
        return new BookWithCommentsDto(book.getId(),
                book.getTitle(),
                AuthorDtoMapper.getDto(book.getAuthor()),
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
