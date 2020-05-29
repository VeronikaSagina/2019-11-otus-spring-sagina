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
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author getById(UUID id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найден автор с id: " + id));
    }

    public List<Author> getAll(String name) {
        return name == null || name.isBlank()
                ? authorRepository.findAll(Sort.by("name"))
                : authorRepository.findAllByNameContainingIgnoreCase(name, Sort.by("name"));
    }

    public Author create(CreateAuthorDto authorDto) {
        Author author = new Author();
        author.setName(authorDto.getName());
        author.setDescription(authorDto.getDescription());
        return authorRepository.save(author);
    }

    @Transactional
    public Author update(UpdateAuthorDto authorDto) {
        return authorRepository.findById(authorDto.getId())
                .map(it -> {
                    Optional.ofNullable(authorDto.getName())
                            .ifPresent(it::setName);
                    Optional.ofNullable(authorDto.getDescription())
                            .ifPresent(it::setDescription);
                    return authorRepository.save(it);
                })
                .orElseThrow(() -> new NotFoundException("Не найден автор с id: " + authorDto.getName()));
    }

    @Transactional
    public void delete(UUID id) {
        authorRepository.findById(id)
                .ifPresentOrElse(authorRepository::delete, () -> {
                    throw new NotFoundException("Не найден автор с id: " + id);
                });
    }
}
