package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import ru.otus.spring.sagina.dto.request.CreateAuthorDto;
import ru.otus.spring.sagina.dto.request.UpdateAuthorDto;
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.repository.AuthorRepository;
import ru.otus.spring.sagina.testdata.AuthorData;

import java.util.List;
import java.util.Optional;


@SpringBootTest
class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private AuthorService authorService;

    @Test
    void createAuthorTest() {
        Mockito.when(authorRepository.save(Mockito.any())).thenReturn(AuthorData.PUSHKIN);
        authorService.createAuthor(new CreateAuthorDto(AuthorData.PUSHKIN.getName()));
        Mockito.verify(authorRepository).save(new Author(null, AuthorData.PUSHKIN.getName()));
    }

    @Test
    void updateAuthorTest() {
        Mockito.when(authorRepository.findById("1")).thenReturn(Optional.of(AuthorData.PELEVIN));
        Mockito.when(authorRepository.save(Mockito.any())).thenReturn(new Author("1", AuthorData.PELEVIN.getName()));
        Author actual = authorService.updateAuthor(new UpdateAuthorDto("1", AuthorData.PELEVIN.getName()));
        Mockito.verify(authorRepository).save(Mockito.any());
        Assertions.assertEquals(AuthorData.TOLSTOY.getId(), actual.getId());
        Assertions.assertEquals(AuthorData.PELEVIN.getName(), actual.getName());
    }

    @Test
    void updateAuthorExceptionTest() {
        Assertions.assertThrows(NotFoundException.class,
                () -> authorService.updateAuthor(new UpdateAuthorDto("1", AuthorData.PELEVIN.getName())));
        Mockito.verify(authorRepository).findById("1");
    }

    @Test
    void deleteAuthorExceptionTest() {
        Mockito.when(authorRepository.findById(AuthorData.PELEVIN.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> authorService.deleteAuthorWithBooks("1"));
        Mockito.verify(authorRepository, Mockito.never()).delete(AuthorData.PELEVIN);
    }

    @Test
    void deleteAuthorWithBooksTest() {
        Mockito.when(authorRepository.findById("1"))
                .thenReturn(Optional.of(AuthorData.TOLSTOY));
        authorService.deleteAuthorWithBooks("1");
        Mockito.verify(authorRepository).delete(AuthorData.TOLSTOY);
    }

    @Test
    void getAuthorByIdTest() {
        Mockito.when(authorRepository.findById("1"))
                .thenReturn(Optional.of(AuthorData.TOLSTOY));
        Author actual = authorService.getAuthorById("1");
        Assertions.assertEquals(AuthorData.TOLSTOY.getName(), actual.getName());
        Assertions.assertEquals(AuthorData.TOLSTOY.getId(), actual.getId());
    }

    @Test
    void getAuthorByNameTest() {
        Mockito.when(authorRepository.findAllByNameContainingIgnoreCaseOrderByName("пушкин")).thenReturn(List.of(AuthorData.PUSHKIN));
        List<Author> actual = authorService.getAuthorByName("пушкин");
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(AuthorData.PUSHKIN.getName(), actual.get(0).getName());
        Assertions.assertEquals(AuthorData.PUSHKIN.getId(), actual.get(0).getId());
    }

    @Test
    void getAllTest() {
        Sort sort = Sort.by("name");
        Mockito.when(authorRepository.findAll(sort)).thenReturn(List.of(AuthorData.TOLSTOY, AuthorData.PUSHKIN));
        List<Author> actual = authorService.getAll();
        Mockito.verify(authorRepository).findAll(sort);
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(AuthorData.TOLSTOY.getName(), actual.get(0).getName());
        Assertions.assertEquals(AuthorData.TOLSTOY.getId(), actual.get(0).getId());
        Assertions.assertEquals(AuthorData.PUSHKIN.getName(), actual.get(1).getName());
        Assertions.assertEquals(AuthorData.PUSHKIN.getId(), actual.get(1).getId());
    }
}
