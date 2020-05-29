package ru.otus.spring.sagina.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.sagina.dto.mapper.BookCommentDtoMapper;
import ru.otus.spring.sagina.dto.request.CreateBookCommentDto;
import ru.otus.spring.sagina.dto.request.UpdateBookCommentDto;
import ru.otus.spring.sagina.dto.response.BookCommentDto;
import ru.otus.spring.sagina.services.BookCommentService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Api(tags = "BookCommentController")
@RestController
public class BookCommentController {
    private final static Logger LOGGER = LoggerFactory.getLogger(BookController.class);
    private final BookCommentService bookCommentService;
    private final BookCommentDtoMapper bookCommentDtoMapper;

    public BookCommentController(BookCommentService bookCommentService,
                                 BookCommentDtoMapper bookCommentDtoMapper) {
        this.bookCommentService = bookCommentService;
        this.bookCommentDtoMapper = bookCommentDtoMapper;
    }

    @GetMapping("/comment/list/{id}")
    @ApiOperation("получить список комментариев по id книги")
    public List<BookCommentDto> getByBookId(@PathVariable("id") UUID id) {
        LOGGER.debug("getting all comments for bookId [{}]", id);
        return bookCommentService.getAllByBookId(id).stream()
                .map(bookCommentDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/comment")
    @ApiOperation("добавить новый комментарий")
    public BookCommentDto addNewBookComment(@RequestBody @Valid CreateBookCommentDto comment) {
        LOGGER.debug("adding a comment");
        return bookCommentDtoMapper.toDto(bookCommentService.create(comment));
    }

    @PatchMapping("/comment")
    @ApiOperation("изменить комментарий")
    public BookCommentDto update(@RequestBody UpdateBookCommentDto bookCommentDto) {
        LOGGER.debug("update comment for bookId [{}]", bookCommentDto.getBookId());
        return bookCommentDtoMapper.toDto(bookCommentService.update(bookCommentDto));
    }

    @DeleteMapping("/comment/{id}")
    @ApiOperation("удалить комментарий")
    public void delete(@PathVariable("id") UUID id) {
        LOGGER.debug("deleting comment with id: [{}]", id);
        bookCommentService.delete(id);
    }
}
