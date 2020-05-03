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
import ru.otus.spring.sagina.entity.BookComment;
import ru.otus.spring.sagina.enums.Type;
import ru.otus.spring.sagina.utils.JsonHelper;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.AFTER_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BookCommentActionsTest {
    @Autowired
    private BookCommentRepository commentRepository;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @PostConstruct
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    void customFindAllByBookIdTest() throws Exception {
        List<BookComment> expected = commentRepository.findAllByBookId(1L);
        ResultActions actions = mockMvc.perform(get("/comment/search/find")
                .queryParam("bookId", "1"))
                .andExpect(status().isOk());

        List<BookComment> actual = JsonHelper.getResultList(actions, Type.COMMENTS);
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getMessage(), actual.get(i).getMessage());
        }
    }

    @Test
    @DirtiesContext(methodMode = AFTER_METHOD)
    void deleteCommentTest() throws Exception {
        Assertions.assertEquals(2, commentRepository.findAllByBookId(1L).size());
        mockMvc.perform(delete("/comment/1"))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));
        Assertions.assertEquals(1, commentRepository.findAllByBookId(1L).size());
    }

    @Test
    void findAllTest() throws Exception {
        List<BookComment> expected = (List<BookComment>) commentRepository.findAll();
        expected.sort(Comparator.comparing(BookComment::getMessage));

        ResultActions actions = mockMvc.perform(get("/comment"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(status().isOk());

        List<BookComment> actual = JsonHelper.getResultList(actions, Type.COMMENTS);
        actual.sort(Comparator.comparing(BookComment::getMessage));
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getMessage(), actual.get(i).getMessage());
        }
    }
}
