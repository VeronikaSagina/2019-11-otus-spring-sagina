package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.sagina.dao.AuthorDao;
import ru.otus.spring.sagina.domain.Author;
import ru.otus.spring.sagina.dto.request.CreateAuthorDto;
import ru.otus.spring.sagina.dto.request.UpdateAuthorDto;
import ru.otus.spring.sagina.dto.response.AuthorDto;
import ru.otus.spring.sagina.exceptions.IllegalOperationException;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.testdata.AuthorData;

import java.util.List;


@SpringBootTest
class AuthorServiceTest {
    @Mock
    private AuthorDao authorDao;
    @Mock
    private BookService bookService;
    @InjectMocks
    private AuthorService authorService;

    @Test
    void createAuthorTest() {
        Mockito.when(authorDao.create(Mockito.any())).thenReturn(AuthorData.PUSHKIN);
        authorService.createAuthor(new CreateAuthorDto(AuthorData.PUSHKIN.getName()));
        Mockito.verify(authorDao).create(new Author(AuthorData.PUSHKIN.getName()));
    }

    @Test
    void updateAuthorTest() {
        Mockito.when(authorDao.update(Mockito.any())).thenReturn(1);
        AuthorDto actual = authorService.updateAuthor(new UpdateAuthorDto(1, AuthorData.PELEVIN.getName()));
        Mockito.verify(authorDao).update(Mockito.any());
        Assertions.assertEquals(AuthorData.TOLSTOY.getId(), actual.id);
        Assertions.assertEquals(AuthorData.PELEVIN.getName(), actual.name);
    }

    @Test
    void updateAuthorExceptionTest() {
        Assertions.assertThrows(NotFoundException.class,
                () -> authorService.updateAuthor(new UpdateAuthorDto(1, AuthorData.PELEVIN.getName())));
    }

    @Test
    void deleteAuthorExceptionTest() {
        Mockito.when(bookService.existsByAuthorId(1)).thenReturn(true);
        Assertions.assertThrows(IllegalOperationException.class, () -> authorService.deleteAuthor(1));
        Mockito.verify(authorDao, Mockito.never()).delete(1);
    }

    @Test
    void deleteAuthorTest() {
        authorService.deleteAuthor(1);
        Mockito.when(bookService.existsByAuthorId(1)).thenReturn(false);
        Mockito.verify(authorDao).delete(1);
    }

    @Test
    void deleteAuthorWithBooksTest() {
        Mockito.when(bookService.existsByAuthorId(1)).thenReturn(true);
        Mockito.when(bookService.getBooksIdByAuthorId(1)).thenReturn(List.of(1));
        authorService.deleteAuthorWithBooks(1);
        Mockito.verify(bookService).deleteBooks(List.of(1));
        Mockito.verify(authorDao).delete(1);
    }

    @Test
    void getAuthorByIdTest() {
        Mockito.when(authorDao.getById(1)).thenReturn(AuthorData.TOLSTOY);
        AuthorDto actual = authorService.getAuthorById(1);
        Assertions.assertEquals(AuthorData.TOLSTOY.getName(), actual.name);
        Assertions.assertEquals(AuthorData.TOLSTOY.getId(), actual.id);
    }

    @Test
    void getAuthorByNameTest() {
        Mockito.when(authorDao.getByName("пушкин")).thenReturn(List.of(AuthorData.PUSHKIN));
        List<AuthorDto> actual = authorService.getAuthorByName("пушкин");
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(AuthorData.PUSHKIN.getName(), actual.get(0).name);
        Assertions.assertEquals(AuthorData.PUSHKIN.getId(), actual.get(0).id);
    }

    @Test
    void getAllTest() {
        Mockito.when(authorDao.getAll()).thenReturn(List.of(AuthorData.TOLSTOY, AuthorData.PUSHKIN));
        List<AuthorDto> actual = authorService.getAll();
        Mockito.verify(authorDao).getAll();
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(AuthorData.TOLSTOY.getName(), actual.get(0).name);
        Assertions.assertEquals(AuthorData.TOLSTOY.getId(), actual.get(0).id);
        Assertions.assertEquals(AuthorData.PUSHKIN.getName(), actual.get(1).name);
        Assertions.assertEquals(AuthorData.PUSHKIN.getId(), actual.get(1).id);
    }
}
