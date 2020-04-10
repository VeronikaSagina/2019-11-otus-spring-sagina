package ru.otus.spring.sagina.dto.mapper;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dto.response.BookDto;
import ru.otus.spring.sagina.entity.Book;

import java.util.stream.Collectors;

@Service
public class BookDtoMapper {
    private final AuthorDtoMapper authorDtoMapper;
    private final GenreDtoMapper genreDtoMapper;

    public BookDtoMapper(AuthorDtoMapper authorDtoMapper, GenreDtoMapper genreDtoMapper) {
        this.authorDtoMapper = authorDtoMapper;
        this.genreDtoMapper = genreDtoMapper;
    }

    public BookDto toDto(Book book) {
        return new BookDto(book.getId(),
                book.getName(),
                book.getDescription(),
                authorDtoMapper.toDto(book.getAuthor()),
                book.getGenres()
                        .stream()
                        .map(genreDtoMapper::toDto)
                        .collect(Collectors.toSet()));
    }
}
