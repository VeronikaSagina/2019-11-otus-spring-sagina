package ru.otus.spring.sagina.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.spring.sagina.BookApplication;
import ru.otus.spring.sagina.dto.mapper.UserDtoMapper;
import ru.otus.spring.sagina.security.SecurityUserDetails;
import ru.otus.spring.sagina.services.UserService;
import ru.otus.spring.sagina.testdata.UserData;

import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = BookApplication.class)
class UserControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private UserService userService;
    @Autowired
    private UserDtoMapper userDtoMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void listUserTest() throws Exception {
        Mockito.when(userService.getListUsers()).thenReturn(List.of(UserData.USER));
        mockMvc.perform(get("/user")
                .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUserDetails(UserData.USER))))
                .andExpect(view().name("users"))
                .andExpect(model().attribute("users", List.of(userDtoMapper.toDto(UserData.USER))))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBookTest() throws Exception {
        mockMvc.perform(post("/user/{id}/delete", 2)
                .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUserDetails(UserData.USER))))
                .andExpect(redirectedUrl("/user"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void lockBookTest() throws Exception {
        mockMvc.perform(post("/user/{id}/lock", 2)
                .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUserDetails(UserData.USER))))
                .andExpect(redirectedUrl("/user"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void UnauthorizedTest() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(redirectedUrl("http://localhost/login"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void logoutTest() throws Exception {
        mockMvc.perform(post("/logout")
                .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUserDetails(UserData.USER))))
                .andExpect(redirectedUrl("/"))
                .andExpect(status().is3xxRedirection());
    }
}