package ru.otus.spring.sagina.controller;

import org.springframework.web.bind.annotation.*;
import ru.otus.spring.sagina.dto.mapper.BookCommentDtoMapper;
import ru.otus.spring.sagina.dto.request.BookCommentDtoRequest;
import ru.otus.spring.sagina.dto.response.BookCommentDto;
import ru.otus.spring.sagina.services.BookCommentService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookCommentController {
    private final BookCommentService bookCommentService;

    public BookCommentController(BookCommentService bookCommentService) {
        this.bookCommentService = bookCommentService;
    }

    @PostMapping("/comment/create")
    public BookCommentDto addNewBookComment(@RequestBody @Valid BookCommentDtoRequest comment) {
        return BookCommentDtoMapper.toDto(bookCommentService.create(comment));
    }

    @GetMapping("/comment/list/{id}")
    public List<BookCommentDto> getByBookId(@PathVariable("id") String id) {
        return bookCommentService.getAllByBookId(id).stream()
                .map(BookCommentDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
