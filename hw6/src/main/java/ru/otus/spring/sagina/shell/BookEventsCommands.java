package ru.otus.spring.sagina.shell;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.sagina.dto.request.CreateBookDto;
import ru.otus.spring.sagina.dto.request.UpdateBookDto;
import ru.otus.spring.sagina.dto.response.BookDto;
import ru.otus.spring.sagina.dto.response.BookWithCommentsDto;
import ru.otus.spring.sagina.services.BookService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@ShellComponent
@ShellCommandGroup("Book commands")
public class BookEventsCommands {
    private final BookService bookService;

    public BookEventsCommands(BookService bookService) {
        this.bookService = bookService;
    }

    @ShellMethod(value = "create book", key = {"cb", "createBook"})
    public BookDto createBook(@ShellOption(
            help = "cb '{\"title\":\"Война и мир\",\"authorId\":1, \"genreIds\":[1]}'") @Valid CreateBookDto book) {
        return bookService.createBook(book);
    }

    @ShellMethod(value = "update book", key = {"ub", "updateBook"})
    public BookDto updateBook(@ShellOption(
            help = "ub '{\"id\":4,\"title\":\"Властелин одного кольца\", \"genreIds\":[2, 4]}'")
                                      @Valid UpdateBookDto book) {
        return bookService.updateBook(book);
    }

    @ShellMethod(value = "getting book by title", key = {"gbt", "getBook"})
    public List<BookDto> getBookByTitle(@ShellOption String title) {
        return bookService.getBookByTitle(title);
    }

    @ShellMethod(value = "getting book by id", key = {"gbi", "getBookById"})
    public BookDto getBookById(@ShellOption int id) {
        return bookService.getBook(id);
    }

    @ShellMethod(value = "getting book by id with all comments", key = {"gbic", "getBookByIdWithComments"})
    public BookWithCommentsDto getBookByIdWithComments(@ShellOption int id) {
        return bookService.getBookWithComments(id);
    }

    @ShellMethod(value = "getting list of books", key = {"allb", "getAllBooks"})
    public List<BookDto> getListBook() {
        return bookService.getAllBooks();
    }

    @ShellMethod(value = "getting list of books by genre", key = {"bbj", "booksByGenreId"})
    public List<BookDto> getListBookByGenreId(int genreId) {
        return bookService.getBooksByGenreId(genreId);
    }

    @ShellMethod(value = "getting books by author", key = {"gba", "getBooksByAuthor"})
    public List<BookDto> getAllBooksByAuthorName(@ShellOption String authorName) {
        return bookService.getAllBooksByAuthorName(authorName);
    }

    @ShellMethod(value = "delete book by id", key = {"db", "deleteBook"})
    public void deleteBook(@ShellOption int bookId) {
        bookService.deleteBooks(Collections.singletonList(bookId));
    }

    @ShellMethod(value = "delete list of books by id", key = {"dbl", "deleteBookList"})
    public void deleteBookList(@ShellOption(help = "dbl '1, 2'") List<Integer> bookIds) {
        bookService.deleteBooks(bookIds);
    }
}
