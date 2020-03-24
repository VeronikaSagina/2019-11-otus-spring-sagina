package ru.otus.spring.sagina.services;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.repository.AuthorRepository;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAll() {
        return authorRepository.findAll(Sort.by("name"));
    }
}
