package ru.otus.spring.sagina.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.otus.spring.sagina.TestLifecycle;
import ru.otus.spring.sagina.dto.mapper.AuthorDtoMapper;
import ru.otus.spring.sagina.dto.response.AuthorDto;
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.utils.JsonHelper;
import ru.otus.spring.sagina.utils.TestUtils;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthorControllerTest extends TestLifecycle {
    @Autowired
    private AuthorController authorController;

    private MockMvc mockMvc;

    @PostConstruct
    private void postConstruct() {
        mockMvc = initMockMvc(authorController);
    }

    @Test
    void getAuthorsTest() throws Exception {
        Comparator<AuthorDto> comparatorById = Comparator.comparing((Function<AuthorDto, String>) a -> a.id);
        ResultActions actions = mockMvc.perform(get("/author"))
                .andExpect(status().isOk());
        List<AuthorDto> actual = JsonHelper.getResultList(actions, AuthorDto.class);
        actual.sort(comparatorById);
        List<AuthorDto> expected = mongoTemplate.findAll(Author.class).stream()
                .map(AuthorDtoMapper::toDto)
                .sorted(comparatorById)
                .collect(Collectors.toList());
        Assertions.assertTrue(TestUtils.compare(expected, actual,
                comparatorById
                        .thenComparing(a -> a.name)));
    }
}