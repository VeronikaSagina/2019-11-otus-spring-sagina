package ru.otus.spring.sagina.controller;

import org.springframework.web.bind.annotation.*;
import ru.otus.spring.sagina.dto.mapper.BookDtoMapper;
import ru.otus.spring.sagina.dto.request.BookDtoRequest;
import ru.otus.spring.sagina.dto.response.BookDto;
import ru.otus.spring.sagina.services.BookService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(value = "/book/create")
    public BookDto createBook(@RequestBody @Valid BookDtoRequest book) {
        return BookDtoMapper.toDto(bookService.createBook(book));
    }

    @PutMapping("/book/update")
    public BookDto updateBook(@RequestBody @Valid BookDtoRequest book) {
        return BookDtoMapper.toDto(bookService.updateBook(book));
    }

    @DeleteMapping("/book/{id}/delete")
    public void deleteBook(@PathVariable("id") String bookId) {
        bookService.deleteBook(bookId);
    }

    @GetMapping("/book/{id}")
    public BookDto getBookById(@PathVariable("id") String id) {
        return BookDtoMapper.toDto(bookService.getBook(id));
    }

    @GetMapping("/book")
    public List<BookDto> getListBook() {
        return bookService.getAllBooks().stream()
                .map(BookDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
