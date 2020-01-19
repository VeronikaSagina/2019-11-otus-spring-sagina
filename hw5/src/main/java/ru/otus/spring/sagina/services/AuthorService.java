package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.sagina.dao.AuthorDao;
import ru.otus.spring.sagina.domain.Author;
import ru.otus.spring.sagina.dto.mapper.AuthorDtoMapper;
import ru.otus.spring.sagina.dto.request.CreateAuthorDto;
import ru.otus.spring.sagina.dto.request.UpdateAuthorDto;
import ru.otus.spring.sagina.dto.response.AuthorDto;
import ru.otus.spring.sagina.exceptions.IllegalOperationException;
import ru.otus.spring.sagina.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorDao authorDao;
    private final BookService bookService;

    public AuthorService(AuthorDao authorDao, BookService bookService) {
        this.authorDao = authorDao;
        this.bookService = bookService;
    }

    @Transactional
    public AuthorDto createAuthor(CreateAuthorDto authorDto) {
        Author created = new Author(authorDto.name);
        return AuthorDtoMapper.getDto(authorDao.create(created));
    }

    @Transactional
    public AuthorDto updateAuthor(UpdateAuthorDto authorDto) {
        Author updated = new Author(authorDto.id, authorDto.name);
        int updatedRow = authorDao.update(updated);
        if (updatedRow == 0) {
            throw new NotFoundException(String.format("не найден автор с id=%s", authorDto.id));
        }
        return AuthorDtoMapper.getDto(updated);
    }

    @Transactional
    public void deleteAuthor(int authorId) {
        if (bookService.existsByAuthorId(authorId)) {
            throw new IllegalOperationException("невозможно выполнить удаление, необходимо удалить все книги этого автора");
        }
        authorDao.delete(authorId);
    }

    @Transactional
    public void deleteAuthorWithBooks(int authorId) {
        if (bookService.existsByAuthorId(authorId)) {
            List<Integer> bookIds = bookService.getBooksIdByAuthorId(authorId);
            bookService.deleteBooks(bookIds);
        }
        authorDao.delete(authorId);
    }

    public AuthorDto getAuthorById(int authorId) {
        return AuthorDtoMapper.getDto(authorDao.getById(authorId));
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
