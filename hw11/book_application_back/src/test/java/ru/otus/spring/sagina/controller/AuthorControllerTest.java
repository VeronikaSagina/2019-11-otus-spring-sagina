package ru.otus.spring.sagina.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.spring.sagina.dto.mapper.AuthorDtoMapper;
import ru.otus.spring.sagina.dto.response.AuthorDto;
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.repository.AuthorRepository;
import ru.otus.spring.sagina.testdata.AuthorData;

import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = AuthorController.class)
class AuthorControllerTest {
    @MockBean
    AuthorRepository repository;
    @Autowired
    private WebTestClient webClient;

    @Test
    void getAuthorsTest() {
        List<Author> authors = List.of(AuthorData.TOLSTOY, AuthorData.PELEVIN);
        Flux<Author> authorFlux = Flux.fromIterable(authors);
        Mockito.when(repository.findAll(Sort.by("name"))).thenReturn(authorFlux);

        webClient.get().uri("/author")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .contains(AuthorDtoMapper.toDto(AuthorData.TOLSTOY), AuthorDtoMapper.toDto(AuthorData.PELEVIN));
        Mockito.verify(repository)
                .findAll(Sort.by("name"));
    }
}