package ru.otus.spring.sagina.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.sagina.dto.mapper.BookDtoMapper;
import ru.otus.spring.sagina.dto.request.BookDtoRequest;
import ru.otus.spring.sagina.dto.response.BookDto;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.repository.AuthorRepository;
import ru.otus.spring.sagina.repository.BookCommentRepository;
import ru.otus.spring.sagina.repository.BookRepository;
import ru.otus.spring.sagina.repository.GenreRepository;
import ru.otus.spring.sagina.testdata.AuthorData;
import ru.otus.spring.sagina.testdata.BookData;
import ru.otus.spring.sagina.testdata.GenreData;

import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = BookController.class)
public class BookControllerTest {
    @MockBean
    private BookCommentRepository bookCommentRepository;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;
    @Autowired
    private WebTestClient webClient;

    @Test
    void createBookTest() {
        BookDtoRequest forCreateDto = BookData.NEW_BOOK_DTO;
        Mockito.when(genreRepository.findAllByIdIn(forCreateDto.genreIds)).thenReturn(Flux.just(GenreData.NOVEL));
        Mockito.when(authorRepository.findById(forCreateDto.author.id)).thenReturn(Mono.just(AuthorData.TOLSTOY));
        Mockito.when(bookRepository.save(BookData.NEW_BOOK)).thenReturn(Mono.just(BookData.NEW_BOOK));

        webClient.post().uri("/book/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(forCreateDto))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .contains(BookDtoMapper.toDto(BookData.NEW_BOOK));
        Mockito.verify(bookRepository)
                .save(BookData.NEW_BOOK);
    }

    @Test
    void updateBookTest() {
        Mockito.when(genreRepository.findAllByIdIn(List.of("1"))).thenReturn(Flux.just(GenreData.NOVEL));
        Mockito.when(authorRepository.findById("1")).thenReturn(Mono.just(AuthorData.TOLSTOY));
        Mockito.when(bookRepository.findById("1")).thenReturn(Mono.just(BookData.UPDATED));
        Mockito.when(bookRepository.save(BookData.UPDATED)).thenReturn(Mono.just(BookData.UPDATED));
        Book expected = new Book(BookData.UPDATED.getId(), BookData.UPDATED.getName(),
                BookData.UPDATED.getDescription(), AuthorData.TOLSTOY, List.of(GenreData.NOVEL), null);

        webClient.put().uri("/book/update")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(BookData.UPDATE_BOOK_DTO))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .contains(BookDtoMapper.toDto(expected));
        Mockito.verify(bookRepository)
                .save(expected);
    }

    @Test
    void deleteBookTest() {
        Mockito.when(bookRepository.findById("1")).thenReturn(Mono.just(BookData.ANNA_KARENINA));
        Mockito.when(bookCommentRepository.deleteAllByBookId("1")).thenReturn(Mono.empty());
        Mockito.when(bookRepository.deleteById("1")).thenReturn(Mono.empty());

        webClient.delete().uri("/book/{id}/delete", 1)
                .exchange()
                .expectStatus().isOk();
        Mockito.verify(bookRepository, Mockito.times(1)).findById("1");
        Mockito.verify(bookCommentRepository).deleteAllByBookId("1");
        Mockito.verify(bookRepository)
                .deleteById("1");
    }

    @Test
    void getBookByIdTest() {
        Mockito.when(bookRepository.findById("1")).thenReturn(Mono.just(BookData.ANNA_KARENINA));

        webClient.get().uri("/book/{id}", "1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .contains(BookDtoMapper.toDto(BookData.ANNA_KARENINA));
        Mockito.verify(bookRepository)
                .findById("1");
    }

    @Test
    void getListBookTest() {
        BookDto actual1 = BookDtoMapper.toDto(BookData.ANNA_KARENINA);
        BookDto actual2 = BookDtoMapper.toDto(BookData.LORD_OF_THE_RINGS);
        List<Book> books = List.of(BookData.ANNA_KARENINA, BookData.LORD_OF_THE_RINGS);
        Flux<Book> bookFlux = Flux.fromIterable(books);
        Mockito.when(bookRepository.findAll(Sort.by("name"))).thenReturn(bookFlux);

        webClient.get().uri("/book")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .contains(actual1, actual2);
        Mockito.verify(bookRepository)
                .findAll(Sort.by("name"));
    }

    @Test
    void createBookException404Test() {
        BookDtoRequest forCreateDto = BookData.NEW_BOOK_DTO;
        Mockito.when(genreRepository.findAllByIdIn(forCreateDto.genreIds)).thenReturn(Flux.just(GenreData.NOVEL));
        Mockito.when(authorRepository.findById(forCreateDto.author.id)).thenReturn(Mono.empty());

        webClient.post().uri("/book/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(forCreateDto))
                .exchange()
                .expectStatus().isNotFound();
        Mockito.verify(bookRepository, Mockito.never())
                .save(Mockito.any());
    }

    @Test
    void updateBookException404Test() {
        Mockito.when(genreRepository.findAllByIdIn(List.of("1"))).thenReturn(Flux.just(GenreData.NOVEL));
        Mockito.when(authorRepository.findById("1")).thenReturn(Mono.just(AuthorData.TOLSTOY));
        Mockito.when(bookRepository.findById("1")).thenReturn(Mono.empty());

        webClient.put().uri("/book/update")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(BookData.UPDATE_BOOK_DTO))
                .exchange()
                .expectStatus().isNotFound();
        Mockito.verify(bookRepository, Mockito.never())
                .save(Mockito.any());
    }

    @Test
    void badGatewayException404Test() {
        webClient.get().uri("/book/delete/path")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void Exception500Test() {
        webClient.get().uri("/book/{id}", "1")
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
