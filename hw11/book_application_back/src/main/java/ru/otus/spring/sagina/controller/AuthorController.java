package ru.otus.spring.sagina.controller;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.spring.sagina.dto.mapper.AuthorDtoMapper;
import ru.otus.spring.sagina.dto.response.AuthorDto;
import ru.otus.spring.sagina.repository.AuthorRepository;

@RestController
public class AuthorController {
   private final AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @GetMapping("/author")
    public Flux<AuthorDto> getAuthors(){
        return authorRepository.findAll(Sort.by("name"))
                .map(AuthorDtoMapper::toDto);
    }
}
