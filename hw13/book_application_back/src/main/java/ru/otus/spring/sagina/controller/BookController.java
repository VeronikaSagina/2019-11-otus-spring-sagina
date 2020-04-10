package ru.otus.spring.sagina.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.sagina.dto.mapper.BookDtoMapper;
import ru.otus.spring.sagina.dto.request.BookDtoRequest;
import ru.otus.spring.sagina.dto.response.BookDto;
import ru.otus.spring.sagina.services.BookService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "BookController")
@RestController
public class BookController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;
    private final BookDtoMapper bookDtoMapper;

    public BookController(BookService bookService,
                          BookDtoMapper bookDtoMapper) {
        this.bookService = bookService;
        this.bookDtoMapper = bookDtoMapper;
    }

    @PostMapping(value = "/book/create")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @ApiOperation("создание книги")
    public BookDto createBook(@RequestBody @Valid BookDtoRequest book) {
        LOGGER.debug("создание книги {}", book.getName());
        return bookDtoMapper.toDto(bookService.createBook(book));
    }

    @PutMapping("/book/update")
    @ApiOperation("редактирование книги")
    @PreAuthorize("hasPermission(#book.id, 'ru.otus.spring.sagina.entity.Book', 'WRITE')")
    public BookDto updateBook(@RequestBody @Valid BookDtoRequest book) {
        LOGGER.debug("редактирование книги с id:{}", book.getId());
        return bookDtoMapper.toDto(bookService.updateBook(book));
    }

    @DeleteMapping("/book/{id}/delete")
    @ApiOperation("удаление книги по id")
    @PreAuthorize("hasPermission(#bookId, 'ru.otus.spring.sagina.entity.Book', 'DELETE')")
    public void deleteBook(@PathVariable("id") Long bookId) {
        LOGGER.debug("удаление книги с id:{}", bookId);
        bookService.deleteBook(bookId);
    }

    @GetMapping("/book/{id}")
    @ApiOperation("получение книги по id")
    @PreAuthorize("hasPermission(#bookId, 'ru.otus.spring.sagina.entity.Book', 'READ')")
    public BookDto getBookById(@PathVariable("id") Long bookId) {
        LOGGER.debug("получение книги с id:{}", bookId);
        return bookDtoMapper.toDto(bookService.getBook(bookId));
    }

    @GetMapping("/book")
    @ApiOperation("получение списка книг")
    public List<BookDto> getListBook() {
        LOGGER.debug("получение списка книг");
        return bookService.getAllBooks().stream()
                .map(bookDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
