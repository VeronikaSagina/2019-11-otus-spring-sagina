package ru.otus.spring.sagina.services;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.sagina.dto.request.CreateAuthorDto;
import ru.otus.spring.sagina.dto.request.UpdateAuthorDto;
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.repository.AuthorRepository;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookService bookService;

    public AuthorService(AuthorRepository authorRepository,
                         BookService bookService) {
        this.authorRepository = authorRepository;
        this.bookService = bookService;
    }

    @Transactional
    public Author createAuthor(CreateAuthorDto authorDto) {
        Author created = new Author();
        created.setName(authorDto.name);
        return authorRepository.save(created);
    }

    @Transactional
    public Author updateAuthor(UpdateAuthorDto authorDto) {
        return authorRepository.findById(authorDto.id).map(it -> {
            it.setName(authorDto.name);
            return authorRepository.save(it);
        }).orElseThrow(() -> new NotFoundException(String.format("не найден автор с id=%s", authorDto.id)));
    }

    @Transactional
    public void deleteAuthorWithBooks(int authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(String.format("не найден автор с id=%s", authorId)));
        bookService.deleteBooksByAuthorId(authorId);
        authorRepository.delete(author);
    }

    public Author getAuthorById(int authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(String.format("не найден автор с id=%s", authorId)));
    }

    public List<Author> getAuthorByName(String name) {
        return authorRepository.findAllByNameContainingIgnoreCaseOrderByName(name);
    }

    public List<Author> getAll() {
        return authorRepository.findAll(Sort.by("name"));
    }
}
