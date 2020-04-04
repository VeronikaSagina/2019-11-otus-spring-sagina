package ru.otus.spring.sagina.dto.mapper;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dto.response.BookWithCommentsDto;
import ru.otus.spring.sagina.entity.Book;

import java.util.stream.Collectors;

@Service
public class BookWithCommentsDtoMapper {
    private final AuthorDtoMapper authorDtoMapper;
    private final BookCommentDtoMapper bookCommentDtoMapper;
    private final GenreDtoMapper genreDtoMapper;

    public BookWithCommentsDtoMapper(AuthorDtoMapper authorDtoMapper,
                                     BookCommentDtoMapper bookCommentDtoMapper,
                                     GenreDtoMapper genreDtoMapper) {
        this.authorDtoMapper = authorDtoMapper;
        this.bookCommentDtoMapper = bookCommentDtoMapper;
        this.genreDtoMapper = genreDtoMapper;
    }

    public BookWithCommentsDto toDto(Book book) {
        return new BookWithCommentsDto(book.getId(),
                book.getTitle(),
                book.getDescription(),
                authorDtoMapper.toDto(book.getAuthor()),
                book.getGenres()
                        .stream()
                        .map(genreDtoMapper::toDto)
                        .collect(Collectors.toSet()),
                book.getComments()
                        .stream()
                        .map(bookCommentDtoMapper::toDto)
                        .collect(Collectors.toList()));
    }
}
