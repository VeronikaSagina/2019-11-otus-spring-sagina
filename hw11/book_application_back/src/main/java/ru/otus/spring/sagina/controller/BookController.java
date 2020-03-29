package ru.otus.spring.sagina.controller;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.sagina.dto.mapper.BookDtoMapper;
import ru.otus.spring.sagina.dto.request.BookDtoRequest;
import ru.otus.spring.sagina.dto.response.BookDto;
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.entity.Genre;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.repository.AuthorRepository;
import ru.otus.spring.sagina.repository.BookCommentRepository;
import ru.otus.spring.sagina.repository.BookRepository;
import ru.otus.spring.sagina.repository.GenreRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
public class BookController {
    private final BookRepository bookRepository;
    private final BookCommentRepository bookCommentRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookController(BookRepository bookRepository,
                          BookCommentRepository bookCommentRepository,
                          AuthorRepository authorRepository,
                          GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.bookCommentRepository = bookCommentRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @PostMapping(value = "/book/create")
    public Mono<BookDto> createBook(@RequestBody @Valid BookDtoRequest book) {
        Mono<List<Genre>> genres = genreRepository.findAllByIdIn(book.genreIds)
                .switchIfEmpty(Mono.error(new NotFoundException("Жанры не найдены")))
                .collectList();
        Mono<Author> author = authorRepository.findById(book.author.id)
                .switchIfEmpty(Mono.error(new NotFoundException("Не найден автор с id: " + book.author.id)));
        return Mono.zip(author, genres)
                .flatMap(tuple2 -> {
                    Book created = new Book();
                    created.setName(book.name);
                    created.setDescription(book.description);
                    created.setAuthor(tuple2.getT1());
                    created.setGenres(tuple2.getT2());
                    return bookRepository.save(created);
                })
                .map(BookDtoMapper::toDto);
    }

    @PutMapping("/book/update")
    public Mono<BookDto> updateBook(@RequestBody @Valid BookDtoRequest book) {
        Mono<List<Genre>> bookGenresMono =
                genreRepository.findAllByIdIn(book.genreIds)
                        .switchIfEmpty(Mono.error(new NotFoundException("Не найдены жанры с id: [" + book.genreIds + "]")))
                        .collectList();
        Mono<Author> author = authorRepository.findById(book.author.id)
                .switchIfEmpty(Mono.error(new NotFoundException("Не найден автор с id: " + book.author.id)));
        return Mono.zip(bookRepository.findById(book.id), author, bookGenresMono)
                .map(tuple3 -> {
                    Book book1 = tuple3.getT1();
                    book1.setName(book.name);
                    book1.setDescription(book.description);
                    book1.setAuthor(tuple3.getT2());
                    book1.setGenres(tuple3.getT3());
                    return book1;
                })
                .flatMap(bookRepository::save)
                .switchIfEmpty(Mono.error(new NotFoundException("не найдена книга с id: " + book.id)))
                .map(BookDtoMapper::toDto);
    }

    @DeleteMapping("/book/{id}/delete")
    public Mono<ResponseEntity<Object>> deleteBook(@PathVariable("id") String bookId) {
        return bookRepository.findById(bookId).flatMap(b ->
                bookCommentRepository.deleteAllByBookId(b.getId())
                        .then(bookRepository.deleteById(b.getId())))
                .then(Mono.just(ResponseEntity.ok(Mono.empty())));
    }

    @GetMapping("/book/{id}")
    public Mono<BookDto> getBookById(@PathVariable("id") String id) {
        return bookRepository.findById(id)
                .map(BookDtoMapper::toDto);
    }

    @GetMapping("/book")
    public Flux<BookDto> getListBook() {
        return bookRepository.findAll(Sort.by("name"))
                .map(BookDtoMapper::toDto);
    }
}
