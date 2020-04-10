package ru.otus.spring.sagina.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.spring.sagina.BookApplication;
import ru.otus.spring.sagina.dto.mapper.AuthorDtoMapper;
import ru.otus.spring.sagina.dto.response.AuthorDto;
import ru.otus.spring.sagina.repository.AuthorRepository;
import ru.otus.spring.sagina.utils.JsonHelper;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = BookApplication.class)
class AuthorControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AuthorDtoMapper authorDtoMapper;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void getAuthorsTest() throws Exception {
        ResultActions actions = mockMvc.perform(get("/author"))
                .andExpect(jsonPath("$", hasSize(7)))
                .andExpect(status().isOk());
        List<AuthorDto> actual = JsonHelper.getResultList(actions, AuthorDto.class);
        List<AuthorDto> expected = authorRepository.findAll(Sort.by("name")).stream()
                .map(authorDtoMapper::toDto)
                .collect(Collectors.toList());
        assertEquals(expected, actual);
    }
}
