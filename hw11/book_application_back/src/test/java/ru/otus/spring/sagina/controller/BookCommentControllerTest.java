package ru.otus.spring.sagina.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.sagina.dto.mapper.BookCommentDtoMapper;
import ru.otus.spring.sagina.dto.request.BookCommentDtoRequest;
import ru.otus.spring.sagina.dto.response.BookCommentDto;
import ru.otus.spring.sagina.repository.BookCommentRepository;
import ru.otus.spring.sagina.repository.BookRepository;
import ru.otus.spring.sagina.testdata.BookCommentData;
import ru.otus.spring.sagina.testdata.BookData;

import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = BookCommentController.class)
class BookCommentControllerTest {
    @MockBean
    private BookCommentRepository bookCommentRepository;
    @MockBean
    private BookRepository bookRepository;
    @Autowired
    private WebTestClient webClient;

    @Test
    void addNewBookCommentTest() {
        BookCommentDtoRequest newComment = new BookCommentDtoRequest(BookCommentData.NEW_COMMENT.getId(),
                BookCommentData.NEW_COMMENT.getBook().getId(),
                BookCommentData.NEW_COMMENT.getMessage());

        Mockito.when(bookRepository.findById("1")).thenReturn(Mono.just(BookData.ANNA_KARENINA));
        Mockito.when(bookCommentRepository.save(BookCommentData.NEW_COMMENT))
                .thenReturn(Mono.just(BookCommentData.NEW_COMMENT));

        webClient.post().uri("/comment/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(newComment))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookCommentDto.class)
                .contains(BookCommentDtoMapper.toDto(BookCommentData.NEW_COMMENT));
        Mockito.verify(bookCommentRepository)
                .save(BookCommentData.NEW_COMMENT);
    }

    @Test
    void getByBookIdTest() {
        Mockito.when(bookCommentRepository.findAllByBookId("1"))
                .thenReturn(Flux.fromIterable(
                        List.of(BookCommentData.KARENINA_COMMENT_1, BookCommentData.KARENINA_COMMENT_2)));

        webClient.get().uri("/comment/list/{id}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookCommentDto.class)
                .contains(BookCommentDtoMapper.toDto(BookCommentData.KARENINA_COMMENT_1),
                        BookCommentDtoMapper.toDto(BookCommentData.KARENINA_COMMENT_2));
        Mockito.verify(bookCommentRepository)
                .findAllByBookId("1");
    }
}
