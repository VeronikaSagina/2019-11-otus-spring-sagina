package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.repository.AuthorRepository;
import ru.otus.spring.sagina.testdata.AuthorData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private AuthorService authorService;

    @Test
    void getAllTest() {
        Sort sort = Sort.by("name");
        Mockito.when(authorRepository.findAll(sort)).thenReturn(List.of(AuthorData.TOLSTOY, AuthorData.PUSHKIN));
        List<Author> actual = authorService.getAll();
        Mockito.verify(authorRepository).findAll(sort);
        assertEquals(2, actual.size());
        assertEquals(List.of(AuthorData.TOLSTOY, AuthorData.PUSHKIN), actual);
    }
}
