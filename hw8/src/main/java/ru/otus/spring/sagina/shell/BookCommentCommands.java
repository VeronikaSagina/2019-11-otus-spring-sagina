package ru.otus.spring.sagina.shell;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.sagina.dto.mapper.BookCommentDtoMapper;
import ru.otus.spring.sagina.dto.request.CreateBookCommentDto;
import ru.otus.spring.sagina.dto.response.BookCommentDto;
import ru.otus.spring.sagina.services.BookCommentService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@ShellCommandGroup("Books comment commands")
public class BookCommentCommands {
    private final BookCommentService bookCommentService;

    public BookCommentCommands(BookCommentService bookCommentService) {
        this.bookCommentService = bookCommentService;
    }

    @ShellMethod(value = "add a new comment to the book", key = {"cc", "createComment"})
    public BookCommentDto addNewBookComment(@ShellOption(help = "cc '{\"message\":\"хорошая книга\", \"bookId\":\"2\"}'")
                                            @Valid CreateBookCommentDto comment) {
        return BookCommentDtoMapper.toDto(bookCommentService.create(comment));
    }

    @ShellMethod(value = "get comment by id", key = {"gc", "getComment"})
    public BookCommentDto getById(@ShellOption String id) {
        return BookCommentDtoMapper.toDto(bookCommentService.getById(id));
    }

    @ShellMethod(value = "get all comments by book id", key = {"allc", "getAllByBookId"})
    public List<BookCommentDto> getAllByBookId(@ShellOption String bookId) {
        return bookCommentService.getAllByBookId(bookId).stream()
                .map(BookCommentDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @ShellMethod(value = "delete comment by id", key = {"dc", "deleteComment"})
    public void deleteById(@ShellOption String bookId) {
        bookCommentService.deleteById(bookId);
    }

    @ShellMethod(value = "delete comment by book id", key = {"dcb", "deleteCommentByBookId"})
    public void deleteAllByBookId(@ShellOption String bookId) {
        bookCommentService.deleteAllByBookId(bookId);
    }
}
