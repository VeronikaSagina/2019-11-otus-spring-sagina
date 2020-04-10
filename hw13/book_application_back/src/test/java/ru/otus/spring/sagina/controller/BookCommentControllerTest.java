package ru.otus.spring.sagina.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.spring.sagina.BookApplication;
import ru.otus.spring.sagina.dto.mapper.BookCommentDtoMapper;
import ru.otus.spring.sagina.dto.request.BookCommentDtoRequest;
import ru.otus.spring.sagina.dto.response.BookCommentDto;
import ru.otus.spring.sagina.repository.BookCommentRepository;
import ru.otus.spring.sagina.utils.JsonHelper;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.AFTER_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = BookApplication.class)
class BookCommentControllerTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private BookCommentDtoMapper bookCommentDtoMapper;
    @Autowired
    private BookCommentRepository bookCommentRepository;
    private MockMvc mockMvc;

    @PostConstruct
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DirtiesContext(methodMode = AFTER_METHOD)
    void addNewBookCommentTest() throws Exception {
        BookCommentDtoRequest expected = new BookCommentDtoRequest(null, 1L, "comment");
        ResultActions actions = mockMvc.perform(post("/comment/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.write(expected)))
                .andExpect(status().isOk());
        BookCommentDto actual = JsonHelper.getResult(actions, BookCommentDto.class);

        assertEquals(expected.getBookId(), actual.getBookId());
        assertEquals(expected.getMessage(), actual.getMessage());
        assertNotNull(actual.getId());
    }

    @Test
    void getByBookIdTest() throws Exception {
        Comparator<BookCommentDto> comparatorById = Comparator.comparing(BookCommentDto::getId);
        ResultActions actions = mockMvc.perform(get("/comment/list/1"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(status().isOk());
        List<BookCommentDto> actual = JsonHelper.getResultList(actions, BookCommentDto.class);
        actual.sort(comparatorById);

        List<BookCommentDto> expected = bookCommentRepository.findAllByBookId(1L)
                .stream()
                .map(bookCommentDtoMapper::toDto)
                .sorted(comparatorById)
                .collect(Collectors.toList());

        assertEquals(expected, actual);
    }
}
