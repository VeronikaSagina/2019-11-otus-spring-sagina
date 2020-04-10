package ru.otus.spring.sagina.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.spring.sagina.BookApplication;
import ru.otus.spring.sagina.dto.mapper.BookDtoMapper;
import ru.otus.spring.sagina.dto.request.BookDtoRequest;
import ru.otus.spring.sagina.dto.response.AuthorDto;
import ru.otus.spring.sagina.dto.response.BookDto;
import ru.otus.spring.sagina.security.UserDetailsAdapter;
import ru.otus.spring.sagina.testdata.BookData;
import ru.otus.spring.sagina.testdata.UserData;
import ru.otus.spring.sagina.utils.JsonHelper;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.AFTER_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = BookApplication.class)
public class BookControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private BookDtoMapper bookDtoMapper;

    @PostConstruct
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DirtiesContext(methodMode = AFTER_METHOD)
    void createBookByAdminTest() throws Exception {
        BookDtoRequest created =
                new BookDtoRequest(null, "name", "description",
                        new AuthorDto(1L, null), List.of(1L));
        mockMvc.perform(post("/book/create")
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.ADMIN_VERONIKA)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.write(created)))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext(methodMode = AFTER_METHOD)
    void createBookByUserTest() throws Exception {
        BookDtoRequest created =
                new BookDtoRequest(null, "name", "description",
                        new AuthorDto(1L, null), List.of(1L));
        mockMvc.perform(post("/book/create")
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.USER_VICTOR)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.write(created)))
                .andExpect(status().isOk());
    }

    @Test
    void createBookByLittleUserFailTest() throws Exception {
        BookDtoRequest created =
                new BookDtoRequest(null, "name", "description",
                        new AuthorDto(1L, null), List.of(1L));
        mockMvc.perform(post("/book/create")
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.USER_NASTYA)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.write(created)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DirtiesContext(methodMode = AFTER_METHOD)
    void updateBookByAdminTest() throws Exception {
        BookDtoRequest expected =
                new BookDtoRequest(1L, "title", "1", new AuthorDto(1L, null), List.of(1L));
        mockMvc.perform(put("/book/update")
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.ADMIN_VERONIKA)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.write(expected)))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext(methodMode = AFTER_METHOD)
    void updateBookByAdminVasyaTest() throws Exception {
        BookDtoRequest expected =
                new BookDtoRequest(1L, "title", "1", new AuthorDto(1L, null), List.of(1L));
        mockMvc.perform(put("/book/update")
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.ADMIN_VASYA)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.write(expected)))
                .andExpect(status().isOk());
    }

    @Test
    void updateBookByUserTest() throws Exception {
        BookDtoRequest expected =
                new BookDtoRequest(1L, "title", "1", new AuthorDto(1L, null), List.of(1L));
        mockMvc.perform(put("/book/update")
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.USER_NASTYA)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.write(expected)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteBookByUserFailTest() throws Exception {
        mockMvc.perform(delete("/book/{id}/delete", 1)
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.USER_NASTYA))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DirtiesContext(methodMode = AFTER_METHOD)
    void deleteBookByAdminTest() throws Exception {
        mockMvc.perform(delete("/book/{id}/delete", 1)
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.ADMIN_VERONIKA))))
                .andExpect(status().isOk());
    }

    @Test
    void getBookByIdByAdminTest() throws Exception {
        ResultActions actions = mockMvc.perform(get("/book/{id}", 1)
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.ADMIN_VERONIKA))))
                .andExpect(status().isOk());

        BookDto actual = JsonHelper.getResult(actions, BookDto.class);
        Assertions.assertEquals(bookDtoMapper.toDto(BookData.ANNA_KARENINA), actual);
    }

    @Test
    void getBookByIdForUserTest() throws Exception {
        mockMvc.perform(get("/book/{id}", 1)
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.USER_NASTYA))))
                .andExpect(status().isForbidden());
    }

    @Test
    void getListBookForAdminTest() throws Exception {
        mockMvc.perform(get("/book")
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.ADMIN_VERONIKA))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)));
    }

    @Test
    void getListBookForUserWithLimitTest() throws Exception {
        mockMvc.perform(get("/book")
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.USER_NASTYA))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void getListBookForUserTest() throws Exception {
        mockMvc.perform(get("/book")
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.USER_VICTOR))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)));
    }
}

