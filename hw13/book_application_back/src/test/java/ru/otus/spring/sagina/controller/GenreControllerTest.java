package ru.otus.spring.sagina.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.spring.sagina.BookApplication;
import ru.otus.spring.sagina.dto.mapper.GenreDtoMapper;
import ru.otus.spring.sagina.dto.response.GenreDto;
import ru.otus.spring.sagina.security.UserDetailsAdapter;
import ru.otus.spring.sagina.testdata.GenreData;
import ru.otus.spring.sagina.testdata.UserData;
import ru.otus.spring.sagina.utils.JsonHelper;

import javax.annotation.PostConstruct;
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
class GenreControllerTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private GenreDtoMapper genreDtoMapper;
    private MockMvc mockMvc;

    @PostConstruct
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void getListGenreTest() throws Exception {
        ResultActions actions = mockMvc.perform(get("/genre")
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.USER_VICTOR))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
        List<GenreDto> actual = JsonHelper.getResultList(actions, GenreDto.class);
        List<GenreDto> expected = List.of(
                GenreData.DETECTIVE, GenreData.KIDS_BOOK_GENRE, GenreData.NOVEL, GenreData.FANTASTIC, GenreData.FANTASY)
                .stream()
                .map(genreDtoMapper::toDto)
                .collect(Collectors.toList());
        assertEquals(expected, actual);
    }
}
