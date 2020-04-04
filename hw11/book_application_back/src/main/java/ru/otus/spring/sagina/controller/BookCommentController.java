package ru.otus.spring.sagina.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.sagina.dto.mapper.BookCommentDtoMapper;
import ru.otus.spring.sagina.dto.request.BookCommentDtoRequest;
import ru.otus.spring.sagina.dto.response.BookCommentDto;
import ru.otus.spring.sagina.entity.BookComment;
import ru.otus.spring.sagina.repository.BookCommentRepository;
import ru.otus.spring.sagina.repository.BookRepository;

import javax.validation.Valid;

@RestController
public class BookCommentController {
    private final BookRepository bookRepository;
    private final BookCommentRepository bookCommentRepository;

    public BookCommentController(BookRepository bookRepository, BookCommentRepository bookCommentRepository) {
        this.bookRepository = bookRepository;
        this.bookCommentRepository = bookCommentRepository;
    }

    @PostMapping("/comment/create")
    public Mono<BookCommentDto> addNewBookComment(@RequestBody @Valid BookCommentDtoRequest comment) {
        return bookRepository.findById(comment.bookId)
                .flatMap(b -> {
                    BookComment bookComment = new  BookComment();
                    bookComment.setMessage(comment.message);
                    bookComment.setBook(b);
                    return bookCommentRepository.save(bookComment);
                })
                .map(BookCommentDtoMapper::toDto);
    }

    @GetMapping("/comment/list/{id}")
    public Flux<BookCommentDto> getByBookId(@PathVariable("id") String id) {
        return bookCommentRepository.findAllByBookId(id)
                .map(BookCommentDtoMapper::toDto);
    }
}
