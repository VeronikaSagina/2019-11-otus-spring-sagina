package ru.otus.spring.sagina.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.spring.sagina.BookApplication;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.entity.BookComment;
import ru.otus.spring.sagina.entity.Genre;
import ru.otus.spring.sagina.enums.Type;
import ru.otus.spring.sagina.utils.JsonHelper;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.rest.webmvc.RestMediaTypes.TEXT_URI_LIST;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.AFTER_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ContextConfiguration(classes = BookApplication.class)
public class BookActionsTest {
    public static final int NO_CONTENT = HttpStatus.NO_CONTENT.value();
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookCommentRepository bookCommentRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;

    @PostConstruct
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    void customFindAllByAuthorIdTest() throws Exception {
        List<Book> expected = bookRepository.findAllByAuthorId(1L);
        expected.sort(Comparator.comparing(Book::getName));

        ResultActions actions = mockMvc.perform(get("/book/search/byAuthor")
                .queryParam("id", "1"))
                .andExpect(status().isOk());

        List<Book> actual = JsonHelper.getResultList(actions, Type.BOOKS);
        actual.sort(Comparator.comparing(Book::getName));
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getName(), actual.get(i).getName());
            assertEquals(expected.get(i).getDescription(), actual.get(i).getDescription());
        }
    }

    @Test
    void getBookByIdTest() throws Exception {
        long bookId = 1L;
        ResultActions actions = mockMvc.perform(get("/book/{id}", bookId))
                .andExpect(status().isOk());
        Book actual = JsonHelper.getResult(actions, Book.class);

        Optional<Book> bookInDb = bookRepository.findById(bookId);
        assertTrue(bookInDb.isPresent());
        Book expected = bookInDb.get();
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    void getAllBookTest() throws Exception {
        ResultActions actions = mockMvc.perform(get("/book"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(status().isOk());

        List<Book> expected = (List<Book>) bookRepository.findAll();
        expected.sort(Comparator.comparing(Book::getName));
        List<Book> actual = JsonHelper.getResultList(actions, Type.BOOKS);
        actual.sort(Comparator.comparing(Book::getName));

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getName(), actual.get(i).getName());
            assertEquals(expected.get(i).getDescription(), actual.get(i).getDescription());
        }
    }

    @Test
    @Transactional
    @DirtiesContext(methodMode = AFTER_METHOD)
    void createBookWithAllFieldsTest() throws Exception {
        long bookId = 7;
        Book created = new Book();
        created.setName("name");
        created.setDescription("description");

        BookComment newComment = new BookComment();
        newComment.setMessage("new comment");

        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.write(created)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlTemplate("http://localhost/book/{id}", bookId));

        mockMvc.perform(put("/book/{id}/author", bookId)
                .contentType(TEXT_URI_LIST)
                .content("/author/1"))
                .andExpect(status().is(NO_CONTENT));

        mockMvc.perform(put("/book/{id}/genres", bookId)
                .contentType(TEXT_URI_LIST)
                .content("/genre/1"))
                .andExpect(status().is(NO_CONTENT));

        mockMvc.perform(post("/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.write(newComment)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlTemplate("http://localhost/comment/{id}", 6));

        mockMvc.perform(put("/book/{id}/comments", bookId)
                .contentType(TEXT_URI_LIST)
                .content("/comment/6"))
                .andExpect(status().is(NO_CONTENT));

        Optional<Book> actualInDb = bookRepository.findById(bookId);
        assertTrue(actualInDb.isPresent());
        Book actual = actualInDb.get();
        assertEquals(created.getName(), actual.getName());
        assertEquals(created.getDescription(), actual.getDescription());
        assertEquals(authorRepository.findById(1L).orElseThrow(), actual.getAuthor());
        assertEquals(genreRepository.findById(1L).orElseThrow(), actual.getGenres().get(0));
        assertEquals(newComment.getMessage(), actual.getComments().get(0).getMessage());
    }

    @Test
    @Transactional
    @DirtiesContext(methodMode = AFTER_METHOD)
    void updateBookTest() throws Exception {
        long bookId = 1L;
        String description = "description";
        Book forUpdate = bookRepository.findById(bookId).orElseThrow();
        assertNotEquals(description, forUpdate.getDescription());
        forUpdate.setDescription(description);

        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.write(forUpdate)))
                .andExpect(status().isCreated());

        Book actual = bookRepository.findById(bookId).orElseThrow();
        assertEquals(description, actual.getDescription());
        assertEquals(forUpdate, actual);
    }

    @Test
    @Transactional
    @DirtiesContext(methodMode = AFTER_METHOD)
    void updateBookGenresTest() throws Exception {
        Genre detective = genreRepository.findById(2L).orElseThrow();
        long bookId = 1L;
        Book forUpdate = bookRepository.findById(bookId).orElseThrow();
        assertEquals(1, forUpdate.getGenres().size());
        assertFalse(forUpdate.getGenres().contains(detective));
        forUpdate.getGenres().add(detective);

        mockMvc.perform(put("/book/{id}/genres", bookId)
                .contentType(TEXT_URI_LIST)
                .content("/genre/1\n/genre/2"))
                .andExpect(status().is(NO_CONTENT));

        Book actual = bookRepository.findById(bookId).orElseThrow();
        assertEquals(forUpdate, actual);
        assertEquals(2, actual.getGenres().size());
        assertTrue(forUpdate.getGenres().contains(detective));
    }

    @Test
    @DirtiesContext(methodMode = AFTER_METHOD)
    void deleteBookTest() throws Exception {
        long bookId = 1L;
        Optional<Book> bookForDelete = bookRepository.findById(bookId);
        assertTrue(bookForDelete.isPresent());
        assertFalse(bookCommentRepository.findAllByBookId(bookId).isEmpty());

        mockMvc.perform(delete("/book/{id}", bookId))
                .andExpect(status().is(NO_CONTENT));

        Optional<Book> actualBook = bookRepository.findById(bookId);
        assertFalse(actualBook.isPresent());
        assertTrue(bookCommentRepository.findAllByBookId(bookId).isEmpty());
    }
}
