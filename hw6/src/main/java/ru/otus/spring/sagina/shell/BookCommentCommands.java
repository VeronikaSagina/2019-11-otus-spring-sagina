package ru.otus.spring.sagina.shell;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.sagina.dto.request.CreateBookCommentDto;
import ru.otus.spring.sagina.dto.response.BookCommentDto;
import ru.otus.spring.sagina.services.BookCommentService;

import javax.validation.Valid;
import java.util.List;

@ShellComponent
@ShellCommandGroup("Books comment commands")
public class BookCommentCommands {
    private final BookCommentService bookCommentService;

    public BookCommentCommands(BookCommentService bookCommentService) {
        this.bookCommentService = bookCommentService;
    }

    @ShellMethod(value = "add a new comment to the book", key = {"cc", "createComment"})
    public BookCommentDto addNewBookComment(@ShellOption(help = "cc '{\"message\":\"хорошая книга\", \"bookId\":2}'")
                                            @Valid CreateBookCommentDto comment) {
        return bookCommentService.create(comment);
    }

    @ShellMethod(value = "get comment by id", key = {"gc", "getComment"})
    public BookCommentDto getById(@ShellOption int id) {
        return bookCommentService.findById(id);
    }

    @ShellMethod(value = "get all comments by book id", key = {"allc", "getAllByBookId"})
    public List<BookCommentDto> getAllByBookId(@ShellOption int bookId) {
        return bookCommentService.getAllByBookId(bookId);
    }

    @ShellMethod(value = "delete comment by id", key = {"dc", "deleteComment"})
    public void deleteById(@ShellOption int bookId) {
        bookCommentService.deleteById(bookId);
    }

    @ShellMethod(value = "delete comment by book id", key = {"dcb", "deleteCommentByBookId"})
    public void deleteAllByBookId(@ShellOption int bookId) {
        bookCommentService.deleteAllByBookId(bookId);
    }
}
