package ru.otus.spring.sagina.services;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dto.request.CreateAuthorDto;
import ru.otus.spring.sagina.dto.request.UpdateAuthorDto;
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.repository.AuthorRepository;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author createAuthor(CreateAuthorDto authorDto) {
        Author created = new Author();
        created.setName(authorDto.name);
        return authorRepository.save(created);
    }

    public Author updateAuthor(UpdateAuthorDto authorDto) {
        Author author = getAuthorById(authorDto.id);
        author.setName(authorDto.name);
        return authorRepository.save(author);
    }

    public void deleteAuthorWithBooks(String authorId) {
        Author author = getAuthorById(authorId);
        authorRepository.delete(author);
    }

    public List<Author> getAuthorByName(String name) {
        return authorRepository.findAllByNameContainingIgnoreCaseOrderByName(name);
    }

    public Author getAuthorById(String authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(String.format("не найден автор с id=%s", authorId)));
    }

    public List<Author> getAll() {
        return authorRepository.findAll(Sort.by("name"));
    }
}
