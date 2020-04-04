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
import ru.otus.spring.sagina.dto.response.GenreDto;
import ru.otus.spring.sagina.entity.Genre;
import ru.otus.spring.sagina.repository.GenreRepository;
import ru.otus.spring.sagina.testdata.GenreData;

import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = GenreController.class)
class GenreControllerTest{
    @MockBean
    private GenreRepository repository;
    @Autowired
    private WebTestClient webClient;

    @Test
    void getGenresTest(){
        List<Genre> genres = List.of(GenreData.DETECTIVE, GenreData.FANTASTIC);
        Flux<Genre> genreFlux = Flux.fromIterable(genres);
        Mockito.when(repository.findAll(Sort.by("name"))).thenReturn(genreFlux);

        webClient.get().uri("/genre")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class);
        Mockito.verify(repository)
                .findAll(Sort.by("name"));
    }
}
