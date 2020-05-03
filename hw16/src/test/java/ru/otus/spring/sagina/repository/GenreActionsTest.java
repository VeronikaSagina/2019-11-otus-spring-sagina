package ru.otus.spring.sagina.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.spring.sagina.entity.Genre;
import ru.otus.spring.sagina.enums.Type;
import ru.otus.spring.sagina.utils.JsonHelper;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.AFTER_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class GenreActionsTest {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @PostConstruct
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    void getAuthorByIdTest() throws Exception {
        long genreId = 1L;
        ResultActions actions = mockMvc.perform(get("/genre/{id}", genreId))
                .andExpect(status().isOk());
        Genre actual = JsonHelper.getResult(actions, Genre.class);

        Genre expected = genreRepository.findById(genreId).orElseThrow();
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getAllTest() throws Exception {
        ResultActions actions = mockMvc.perform(get("/genre"))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(status().isOk());

        List<Genre> expected = (List<Genre>) genreRepository.findAll();
        expected.sort(Comparator.comparing(Genre::getName));
        List<Genre> actual = JsonHelper.getResultList(actions, Type.GENRES);
        actual.sort(Comparator.comparing(Genre::getName));

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getName(), actual.get(i).getName());
        }
    }

    @Test
    @DirtiesContext(methodMode = AFTER_METHOD)
    void deleteGenreTest() throws Exception {
        long genreId = 1L;
        Optional<Genre> genreForDelete = genreRepository.findById(genreId);
        assertTrue(genreForDelete.isPresent());

        mockMvc.perform(delete("/genre/{id}", genreId))
                .andExpect(status().isMethodNotAllowed());

        Optional<Genre> actualGenre = genreRepository.findById(genreId);
        assertTrue(actualGenre.isPresent());
    }
}
