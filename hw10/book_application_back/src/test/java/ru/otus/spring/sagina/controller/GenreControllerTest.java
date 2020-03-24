package ru.otus.spring.sagina.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.otus.spring.sagina.TestLifecycle;
import ru.otus.spring.sagina.dto.mapper.GenreDtoMapper;
import ru.otus.spring.sagina.dto.response.GenreDto;
import ru.otus.spring.sagina.entity.Genre;
import ru.otus.spring.sagina.utils.JsonHelper;
import ru.otus.spring.sagina.utils.TestUtils;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GenreControllerTest extends TestLifecycle {
    @Autowired
    private GenreController genreController;

    private MockMvc mockMvc;

    @PostConstruct
    private void postConstruct() {
        mockMvc = initMockMvc(genreController);
    }

    @Test
    void getGenresTest() throws Exception {
        Comparator<GenreDto> comparatorById = Comparator.comparing((Function<GenreDto, String>) g -> g.id);
        ResultActions actions = mockMvc.perform(get("/genre"))
                .andExpect(status().isOk());
        List<GenreDto> actual = JsonHelper.getResultList(actions, GenreDto.class);
        actual.sort(comparatorById);
        List<GenreDto> expected = mongoTemplate.findAll(Genre.class).stream()
                .map(GenreDtoMapper::toDto)
                .sorted(comparatorById)
                .collect(Collectors.toList());
        Assertions.assertTrue(TestUtils.compare(expected, actual,
                comparatorById
                        .thenComparing(g -> g.name)));
    }
}