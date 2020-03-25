package ru.otus.spring.sagina.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.otus.spring.sagina.TestLifecycle;
import ru.otus.spring.sagina.dto.mapper.BookDtoMapper;
import ru.otus.spring.sagina.dto.request.BookDtoRequest;
import ru.otus.spring.sagina.dto.response.AuthorDto;
import ru.otus.spring.sagina.dto.response.BookDto;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.entity.Genre;
import ru.otus.spring.sagina.utils.JsonHelper;
import ru.otus.spring.sagina.utils.TestUtils;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookControllerTest extends TestLifecycle {
    @Autowired
    private BookController bookController;

    private MockMvc mockMvc;

    @PostConstruct
    private void postConstruct() {
        mockMvc = initMockMvc(bookController);
    }

    @Test
    void getAllTestTest() throws Exception {
        Comparator<BookDto> comparatorById = Comparator.comparing((Function<BookDto, String>) b -> b.id);
        ResultActions actions = mockMvc.perform(get("/book"))
                .andExpect(status().isOk());
        List<BookDto> actual = JsonHelper.getResultList(actions, BookDto.class);
        actual.sort(comparatorById);
        List<BookDto> expected = mongoTemplate.findAll(Book.class).stream()
                .map(BookDtoMapper::toDto)
                .sorted(comparatorById)
                .collect(Collectors.toList());
        Assertions.assertTrue(TestUtils.compare(expected, actual,
                comparatorById
                        .thenComparing(b -> b.description)
                        .thenComparing(b -> b.name)
                        .thenComparing(b -> b.author.id)
                        .thenComparing(b -> b.genres.size())));
    }

    @Test
    void createBookTest() throws Exception {
        BookDtoRequest expected = new BookDtoRequest(null, "title", "description",
                new AuthorDto("1", null), List.of("1"));
        ResultActions actions = mockMvc.perform(post("/book/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.write(expected)))
                .andExpect(status().isOk());
        Book actual = JsonHelper.getResult(actions, Book.class);
        Assertions.assertEquals(expected.name, actual.getName());
        Assertions.assertEquals(expected.author.id, actual.getAuthor().getId());
        Assertions.assertEquals(expected.description, actual.getDescription());
        Assertions.assertEquals(expected.genreIds.stream().sorted().collect(Collectors.toList()),
                actual.getGenres().stream().map(Genre::getId).sorted().collect(Collectors.toList()));
    }

    @Test
    void updateBookTest() throws Exception {
        BookDtoRequest expected =
                new BookDtoRequest("1", "title", "1", new AuthorDto("1", null), List.of("1"));
        mockMvc.perform(put("/book/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.write(expected)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteBookTest() throws Exception {
        Assertions.assertTrue(Optional.ofNullable(mongoTemplate.findById("1", Book.class)).isPresent());
        mockMvc.perform(delete("/book/1/delete"))
                .andExpect(status().isOk());
        Assertions.assertTrue(Optional.ofNullable(mongoTemplate.findById("1", Book.class)).isEmpty());
    }

    @Test
    void getBookByIdTest() throws Exception {
        ResultActions actions = mockMvc.perform(get("/book/1"))
                .andExpect(status().isOk());
        BookDto actual = JsonHelper.getResult(actions, BookDto.class);
        Optional<Book> actualInDb = Optional.ofNullable(mongoTemplate.findById("1", Book.class));
        Assertions.assertTrue(actualInDb.isPresent());
        BookDto expected = BookDtoMapper.toDto(actualInDb.get());
        Assertions.assertEquals(expected.id, actual.id);
        Assertions.assertEquals(expected.description, actual.description);
        Assertions.assertEquals(expected.name, actual.name);
        Assertions.assertEquals(expected.author.id, actual.author.id);
        Assertions.assertEquals(expected.genres.stream().map(g -> g.id).sorted().collect(Collectors.toList()),
                actual.genres.stream().map(g -> g.id).sorted().collect(Collectors.toList()));
    }
}
