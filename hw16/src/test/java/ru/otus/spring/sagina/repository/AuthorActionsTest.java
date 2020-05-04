package ru.otus.spring.sagina.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.enums.Type;
import ru.otus.spring.sagina.utils.JsonHelper;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.AFTER_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthorActionsTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    @PostConstruct
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    void customFindMethodTest() throws Exception {
        List<Author> expected = List.of(new Author(null, "Анджей Сапко́вский"));

        ResultActions actions = mockMvc.perform(get("/author/search/byName")
                .queryParam("name", "анджей"))
                .andExpect(status().isOk());

        List<Author> actual = JsonHelper.getResultList(actions, Type.AUTHORS);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAuthorByIdTest() throws Exception {
        long authorId = 1L;
        ResultActions actions = mockMvc.perform(get("/author/{id}", authorId))
                .andExpect(status().isOk());
        Book actual = JsonHelper.getResult(actions, Book.class);

        Author expected = authorRepository.findById(authorId).orElseThrow();
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getAllTest() throws Exception {
        ResultActions actions = mockMvc.perform(get("/author"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(status().isOk());

        List<Author> expected = (List<Author>) authorRepository.findAll();
        expected.sort(Comparator.comparing(Author::getName));
        List<Author> actual = JsonHelper.getResultList(actions, Type.AUTHORS);
        actual.sort(Comparator.comparing(Author::getName));

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getName(), actual.get(i).getName());
        }
    }

    @Test
    @DirtiesContext(methodMode = AFTER_METHOD)
    void deleteAuthorTest() throws Exception {
        long authorId = 1L;
        Optional<Author> authorForDelete = authorRepository.findById(authorId);
        assertTrue(authorForDelete.isPresent());
        assertFalse(bookRepository.findAllByAuthorId(authorId).isEmpty());

        mockMvc.perform(delete("/author/{id}", authorId))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

        Optional<Author> actualAuthor = authorRepository.findById(authorId);
        assertFalse(actualAuthor.isPresent());
        assertTrue(bookRepository.findAllByAuthorId(authorId).isEmpty());
    }
}
