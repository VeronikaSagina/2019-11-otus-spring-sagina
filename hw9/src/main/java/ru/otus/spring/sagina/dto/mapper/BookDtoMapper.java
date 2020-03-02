package ru.otus.spring.sagina.dto.mapper;

import ru.otus.spring.sagina.dto.response.BookDto;
import ru.otus.spring.sagina.entity.Book;

import java.util.stream.Collectors;

public class BookDtoMapper {
    public static BookDto toDto(Book book) {
        return new BookDto(book.getId(),
                book.getTitle(),
                book.getDescription(),
                AuthorDtoMapper.toDto(book.getAuthor()),
                book.getGenres()
                        .stream()
                        .map(GenreDtoMapper::toDto)
                        .collect(Collectors.toSet()));
    }

    public static BookDto emptyDto(){
        return new BookDto(null, null,null, null, null);
    }
}
