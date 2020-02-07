package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.sagina.dao.AuthorDao;
import ru.otus.spring.sagina.domain.Author;
import ru.otus.spring.sagina.dto.mapper.AuthorDtoMapper;
import ru.otus.spring.sagina.dto.request.CreateAuthorDto;
import ru.otus.spring.sagina.dto.request.UpdateAuthorDto;
import ru.otus.spring.sagina.dto.response.AuthorDto;
import ru.otus.spring.sagina.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorDao authorDao;

    public AuthorService(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Transactional
    public AuthorDto createAuthor(CreateAuthorDto authorDto) {
        Author created = new Author(authorDto.name);
        return AuthorDtoMapper.getDto(authorDao.save(created));
    }

    @Transactional
    public AuthorDto updateAuthor(UpdateAuthorDto authorDto) {
        if (authorDao.findById(authorDto.id).isEmpty()) {
            throw new NotFoundException(String.format("не найден автор с id=%s", authorDto.id));
        }
        Author saved = authorDao.save(new Author(authorDto.id, authorDto.name));
        return AuthorDtoMapper.getDto(saved);
    }

    @Transactional
    public void deleteAuthorWithBooks(int authorId) {
        Author author = authorDao.findByIdWithBooks(authorId)
                .orElseThrow(() -> new NotFoundException(String.format("не найден автор с id=%s", authorId)));
        authorDao.delete(author);
    }

    public AuthorDto getAuthorById(int authorId) {
        return AuthorDtoMapper.getDto(authorDao.findById(authorId)
                .orElseThrow(() -> new NotFoundException(String.format("не найден автор с id=%s", authorId))));
    }

    public List<AuthorDto> getAuthorByName(String name) {
        return authorDao.getByName(name).stream()
                .map(AuthorDtoMapper::getDto)
                .collect(Collectors.toList());
    }

    public List<AuthorDto> getAll() {
        return authorDao.getAll().stream()
                .map(AuthorDtoMapper::getDto)
                .collect(Collectors.toList());
    }
}
