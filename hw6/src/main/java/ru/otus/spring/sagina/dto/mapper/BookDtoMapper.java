package ru.otus.spring.sagina.dto.mapper;

import ru.otus.spring.sagina.domain.Book;
import ru.otus.spring.sagina.dto.response.BookDto;

import java.util.stream.Collectors;

public class BookDtoMapper {
    public static BookDto toDto(Book book) {
        return new BookDto(book.getId(),
                book.getTitle(),
                AuthorDtoMapper.getDto(book.getAuthor()),
                book.getGenres()
                        .stream()
                        .map(GenreDtoMapper::toDto)
                        .collect(Collectors.toSet()));
    }
}
