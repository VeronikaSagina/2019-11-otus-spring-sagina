package ru.otus.spring.sagina.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.spring.sagina.dto.mapper.AuthorDtoMapper;
import ru.otus.spring.sagina.dto.mapper.BookDtoMapper;
import ru.otus.spring.sagina.dto.mapper.BookWithCommentsDtoMapper;
import ru.otus.spring.sagina.dto.mapper.GenreDtoMapper;
import ru.otus.spring.sagina.dto.request.BookDtoRequest;
import ru.otus.spring.sagina.dto.response.AuthorDto;
import ru.otus.spring.sagina.dto.response.BookDto;
import ru.otus.spring.sagina.dto.response.BookWithCommentsDto;
import ru.otus.spring.sagina.dto.response.GenreDto;
import ru.otus.spring.sagina.services.AuthorService;
import ru.otus.spring.sagina.services.BookService;
import ru.otus.spring.sagina.services.GenreService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookController {
    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;

    public BookController(BookService bookService, GenreService genreService, AuthorService authorService) {
        this.bookService = bookService;
        this.genreService = genreService;
        this.authorService = authorService;
    }

    @GetMapping("/book")
    public String listBook(Model model) {
        List<BookDto> books = bookService.getAllBooks().stream()
                .map(BookDtoMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("books", books);
        return "book/books";
    }

    @GetMapping("/book/{id}")
    public String getBook(@PathVariable("id") String id, Model model) {
        BookWithCommentsDto book = BookWithCommentsDtoMapper.toDto(bookService.getBookWithComments(id));
        model.addAttribute("book", book);
        return "book/book";
    }

    @GetMapping(value = {"/book/{id}/edit", "/book/create"})
    public String editBook(@PathVariable(value = "id", required = false) String id, Model model) {
        BookDto book;
        if (id != null) {
            book = BookDtoMapper.toDto(bookService.getBook(id));
        } else {
            book = BookDtoMapper.emptyDto();
        }
        List<AuthorDto> authors = authorService.getAll().stream()
                .map(AuthorDtoMapper::toDto)
                .collect(Collectors.toList());
        List<GenreDto> genres = genreService.getAll().stream()
                .map(GenreDtoMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book/edit";
    }

    @PostMapping("/book/edit")
    public String updateBook(@ModelAttribute("book") @Valid BookDtoRequest bookDtoRequest,
                             RedirectAttributes attributes) {
        String id;
        if (bookDtoRequest.id == null || bookDtoRequest.id.isBlank()) {
            id = bookService.createBook(bookDtoRequest).getId();
        } else {
            id = bookService.updateBook(bookDtoRequest).getId();
        }
        attributes.addAttribute("id", id);
        return "redirect:/book/{id}";
    }

    @PostMapping("/book/{id}/delete")
    public String deleteBook(@PathVariable("id") String id) {
        bookService.deleteBook(id);
        return "redirect:/book";
    }
}
