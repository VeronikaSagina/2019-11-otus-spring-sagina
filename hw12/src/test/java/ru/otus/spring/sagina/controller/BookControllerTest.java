package ru.otus.spring.sagina.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.spring.sagina.BookApplication;
import ru.otus.spring.sagina.dto.mapper.AuthorDtoMapper;
import ru.otus.spring.sagina.dto.mapper.BookDtoMapper;
import ru.otus.spring.sagina.dto.mapper.BookWithCommentsDtoMapper;
import ru.otus.spring.sagina.dto.mapper.GenreDtoMapper;
import ru.otus.spring.sagina.dto.request.BookDtoRequest;
import ru.otus.spring.sagina.security.SecurityUserDetails;
import ru.otus.spring.sagina.services.AuthorService;
import ru.otus.spring.sagina.services.BookService;
import ru.otus.spring.sagina.services.GenreService;
import ru.otus.spring.sagina.testdata.AuthorData;
import ru.otus.spring.sagina.testdata.BookData;
import ru.otus.spring.sagina.testdata.GenreData;
import ru.otus.spring.sagina.testdata.UserData;

import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = BookApplication.class)
class BookControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;
    @Autowired
    private BookDtoMapper bookDtoMapper;
    @Autowired
    private AuthorDtoMapper authorDtoMapper;
    @Autowired
    private GenreDtoMapper genreDtoMapper;
    @Autowired
    private BookWithCommentsDtoMapper bookWithCommentsDtoMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void listBookTest() throws Exception {
        mockMvc.perform(get("/book")
                .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUserDetails(UserData.USER))))
                .andExpect(view().name("book/books"))
                .andExpect(model().attributeExists("books"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookTest() throws Exception {
        Mockito.when(bookService.getBookWithComments("1")).thenReturn(BookData.ANNA_KARENINA);
        mockMvc.perform(get("/book/{id}", 1)
                .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUserDetails(UserData.USER))))
                .andExpect(view().name("book/book"))
                .andExpect(model().attribute("book", bookWithCommentsDtoMapper.toDto(BookData.ANNA_KARENINA)))
                .andExpect(status().isOk());
    }

    @Test
    void editBookGetTest() throws Exception {
        Mockito.when(bookService.getBook("1")).thenReturn(BookData.ANNA_KARENINA);
        Mockito.when(authorService.getAll()).thenReturn(List.of(AuthorData.CHRISTIE));
        Mockito.when(genreService.getAll()).thenReturn(List.of(GenreData.NOVEL));
        mockMvc.perform(get("/book/{id}/edit", 1)
                .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUserDetails(UserData.USER))))
                .andExpect(view().name("book/edit"))
                .andExpect(model().attribute("book", bookDtoMapper.toDto(BookData.ANNA_KARENINA)))
                .andExpect(model().attribute("authors", List.of(authorDtoMapper.toDto(AuthorData.CHRISTIE))))
                .andExpect(model().attribute("genres", List.of(genreDtoMapper.toDto(GenreData.NOVEL))))
                .andExpect(status().isOk());
    }

    @Test
    void createBookGetTest() throws Exception {
        Mockito.when(authorService.getAll()).thenReturn(List.of(AuthorData.CHRISTIE));
        Mockito.when(genreService.getAll()).thenReturn(List.of(GenreData.NOVEL));
        mockMvc.perform(get("/book/create")
                .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUserDetails(UserData.USER))))
                .andExpect(view().name("book/edit"))
                .andExpect(model().attribute("book", bookDtoMapper.emptyDto()))
                .andExpect(model().attribute("authors", List.of(authorDtoMapper.toDto(AuthorData.CHRISTIE))))
                .andExpect(model().attribute("genres", List.of(genreDtoMapper.toDto(GenreData.NOVEL))))
                .andExpect(status().isOk());
    }

    @Test
    void UnauthorizedTest() throws Exception {
        mockMvc.perform(get("/book"))
                .andExpect(redirectedUrl("http://localhost/login"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void updateBookTest() throws Exception {
        BookDtoRequest forUpdate =
                new BookDtoRequest("1", "title", "1", "description", List.of("1"));
        Mockito.when(bookService.updateBook(forUpdate)).thenReturn(BookData.ANNA_KARENINA);
        mockMvc.perform(post("/book/edit")
                .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUserDetails(UserData.USER)))
                .flashAttr("book", forUpdate))
                .andExpect(redirectedUrlTemplate("/book/{id}", "1"))
                .andExpect(status().is3xxRedirection());
        Mockito.verify(bookService, Mockito.never()).createBook(forUpdate);
    }

    @Test
    void updateBookCreateTest() throws Exception {
        BookDtoRequest forCreate =
                new BookDtoRequest(null, "title", "1", "description", List.of("1"));
        Mockito.when(bookService.createBook(forCreate)).thenReturn(BookData.ANNA_KARENINA);
        mockMvc.perform(post("/book/edit")
                .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUserDetails(UserData.USER)))
                .flashAttr("book", forCreate))
                .andExpect(redirectedUrlTemplate("/book/{id}", "1"))
                .andExpect(status().is3xxRedirection());
        Mockito.verify(bookService, Mockito.never()).updateBook(forCreate);
    }

    @Test
    void deleteBookTest() throws Exception {
        mockMvc.perform(post("/book/{id}/delete", 2)
                .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUserDetails(UserData.USER))))
                .andExpect(redirectedUrl("/book"))
                .andExpect(status().is3xxRedirection());
    }
}