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
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.testdata.AuthorData;

import java.util.List;
import java.util.Optional;


@SpringBootTest
class AuthorServiceTest {
    @Mock
    private AuthorDao authorDao;
    @InjectMocks
    private AuthorService authorService;

    @Test
    void createAuthorTest() {
        Mockito.when(authorDao.save(Mockito.any())).thenReturn(AuthorData.PUSHKIN);
        authorService.createAuthor(new CreateAuthorDto(AuthorData.PUSHKIN.getName()));
        Mockito.verify(authorDao).save(new Author(AuthorData.PUSHKIN.getName()));
    }

    @Test
    void updateAuthorTest() {
        Mockito.when(authorDao.findById(1)).thenReturn(Optional.of(AuthorData.PELEVIN));
        Mockito.when(authorDao.save(Mockito.any())).thenReturn(new Author(1, AuthorData.PELEVIN.getName()));
        AuthorDto actual = authorService.updateAuthor(new UpdateAuthorDto(1, AuthorData.PELEVIN.getName()));
        Mockito.verify(authorDao).save(Mockito.any());
        Assertions.assertEquals(AuthorData.TOLSTOY.getId(), actual.id);
        Assertions.assertEquals(AuthorData.PELEVIN.getName(), actual.name);
    }

    @Test
    void updateAuthorExceptionTest() {
        Assertions.assertThrows(NotFoundException.class,
                () -> authorService.updateAuthor(new UpdateAuthorDto(1, AuthorData.PELEVIN.getName())));
        Mockito.verify(authorDao).findById(1);
    }

    @Test
    void deleteAuthorExceptionTest() {
        Mockito.when(authorDao.findById(AuthorData.PELEVIN.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> authorService.deleteAuthorWithBooks(1));
        Mockito.verify(authorDao, Mockito.never()).delete(AuthorData.PELEVIN);
    }

    @Test
    void deleteAuthorWithBooksTest() {
        Mockito.when(authorDao.findByIdWithBooks(1))
                .thenReturn(Optional.of(AuthorData.TOLSTOY));
        authorService.deleteAuthorWithBooks(1);
        Mockito.verify(authorDao).delete(AuthorData.TOLSTOY);
    }

    @Test
    void getAuthorByIdTest() {
        Mockito.when(authorDao.findById(1))
                .thenReturn(Optional.of(AuthorData.TOLSTOY));
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
