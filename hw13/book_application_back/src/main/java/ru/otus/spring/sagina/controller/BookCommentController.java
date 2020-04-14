package ru.otus.spring.sagina.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.sagina.dto.mapper.BookCommentDtoMapper;
import ru.otus.spring.sagina.dto.request.BookCommentDtoRequest;
import ru.otus.spring.sagina.dto.response.BookCommentDto;
import ru.otus.spring.sagina.services.BookCommentService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "BookCommentController")
@RestController
public class BookCommentController {
    private final BookCommentService bookCommentService;
    private final BookCommentDtoMapper bookCommentDtoMapper;

    public BookCommentController(BookCommentService bookCommentService,
                                 BookCommentDtoMapper bookCommentDtoMapper) {
        this.bookCommentService = bookCommentService;
        this.bookCommentDtoMapper = bookCommentDtoMapper;
    }

    @PostMapping("/comment/create")
    @ApiOperation("добавление нового комментария к книге")
    public BookCommentDto addNewBookComment(@RequestBody @Valid BookCommentDtoRequest comment) {
        return bookCommentDtoMapper.toDto(bookCommentService.create(comment));
    }

    @GetMapping("/comment/list/{id}")
    @ApiOperation("получение списка комментариев по id книги")
    public List<BookCommentDto> getByBookId(@PathVariable("id") Long id) {
        return bookCommentService.getAllByBookId(id).stream()
                .map(bookCommentDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
