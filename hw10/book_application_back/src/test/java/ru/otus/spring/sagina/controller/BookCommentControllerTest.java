package ru.otus.spring.sagina.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.otus.spring.sagina.TestLifecycle;
import ru.otus.spring.sagina.dto.mapper.BookCommentDtoMapper;
import ru.otus.spring.sagina.dto.request.BookCommentDtoRequest;
import ru.otus.spring.sagina.dto.response.BookCommentDto;
import ru.otus.spring.sagina.entity.BookComment;
import ru.otus.spring.sagina.utils.JsonHelper;
import ru.otus.spring.sagina.utils.TestUtils;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookCommentControllerTest extends TestLifecycle {
    @Autowired
    private BookCommentController bookCommentController;

    private MockMvc mockMvc;

    @PostConstruct
    private void postConstruct() {
        mockMvc = initMockMvc(bookCommentController);
    }

    @Test
    void addNewBookCommentTest() throws Exception {
        BookCommentDtoRequest expected = new BookCommentDtoRequest(null, "1", "comment");
        ResultActions actions = mockMvc.perform(post("/comment/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.write(expected)))
                .andExpect(status().isOk());

        BookCommentDto actual = JsonHelper.getResult(actions, BookCommentDto.class);
        Assertions.assertEquals(expected.bookId, actual.bookId);
        Assertions.assertEquals(expected.message, actual.message);
        Assertions.assertNotNull(actual.id);

        Optional<BookComment> actualInDb = Optional.ofNullable(mongoTemplate.findById(actual.id, BookComment.class));
        Assertions.assertTrue(actualInDb.isPresent());
        Assertions.assertEquals(actualInDb.get().getBook().getId(), actual.bookId);
        Assertions.assertEquals(actualInDb.get().getId(), actual.id);
        Assertions.assertEquals(actualInDb.get().getMessage(), actual.message);
    }

    @Test
    void getByBookIdTest() throws Exception {
        Comparator<BookCommentDto> comparatorById = Comparator.comparing((Function<BookCommentDto, String>) b -> b.id);
        ResultActions actions = mockMvc.perform(get("/comment/list/1"))
                .andExpect(status().isOk());
        List<BookCommentDto> actual = JsonHelper.getResultList(actions, BookCommentDto.class);
        actual.sort(comparatorById);

        List<BookCommentDto> expected = mongoTemplate
                .find(new Query().addCriteria(Criteria.where("bookId").is("1")), BookComment.class).stream()
                .map(BookCommentDtoMapper::toDto)
                .sorted(comparatorById)
                .collect(Collectors.toList());

        Assertions.assertTrue(TestUtils.compare(expected, actual,
                comparatorById
                        .thenComparing(b -> b.message)
                        .thenComparing(b -> b.bookId)));
    }
}