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
import ru.otus.spring.sagina.dto.mapper.UserDtoMapper;
import ru.otus.spring.sagina.dto.response.UserDto;
import ru.otus.spring.sagina.entity.User;
import ru.otus.spring.sagina.repository.UserRepository;
import ru.otus.spring.sagina.security.UserDetailsAdapter;
import ru.otus.spring.sagina.testdata.UserData;
import ru.otus.spring.sagina.utils.JsonHelper;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = BookApplication.class)
public class UserControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDtoMapper userDtoMapper;

    @PostConstruct
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void getListUserForAdminTest() throws Exception {
        mockMvc.perform(get("/user")
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.ADMIN_VERONIKA))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    void getListUserForAdminVasyaTest() throws Exception {
        mockMvc.perform(get("/user")
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.ADMIN_VASYA))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    void getListUserFailTest() throws Exception {
        mockMvc.perform(get("/user")
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.USER_VICTOR))))
                .andExpect(status().isForbidden());
    }

    @Test
    void getListLittleUserFailTest() throws Exception {
        mockMvc.perform(get("/user")
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.USER_NASTYA))))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUserByAdminTest() throws Exception {
        mockMvc.perform(delete("/user/{id}/delete", 4)
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.ADMIN_VERONIKA))))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUserByAdminFailTest() throws Exception {
        mockMvc.perform(delete("/user/{id}/delete", 4)
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.ADMIN_VASYA))))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUserByUserTest() throws Exception {
        mockMvc.perform(delete("/book/{id}/delete", 4)
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.USER_VICTOR))))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void adminBlocksHimselfTest() throws Exception {
        mockMvc.perform(post("/user/{id}/lock", 1)
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.ADMIN_VERONIKA))))
                .andExpect(status().isForbidden());

        User expected = userRepository.getOne(1L);
        assertFalse(expected.isLocked());
    }

    @Test
    @Transactional
    void lockUserByAdminTest() throws Exception {
        User expected = userRepository.getOne(4L);
        assertFalse(expected.isLocked());
        expected.setLocked(true);

        ResultActions actions = mockMvc.perform(post("/user/{id}/lock", 4)
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.ADMIN_VERONIKA))))
                .andExpect(status().isOk());

        UserDto actual = JsonHelper.getResult(actions, UserDto.class);
        assertEquals(userDtoMapper.toDto(expected), actual);
    }

    @Test
    @Transactional
    void lockUserByAdminFailTest() throws Exception {
        mockMvc.perform(post("/user/{id}/lock", 1)
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.ADMIN_VASYA))))
                .andExpect(status().isForbidden());

        User expected = userRepository.getOne(1L);
        assertFalse(expected.isLocked());
    }

    @Test
    @Transactional
    void lockUserByUserTest() throws Exception {
        mockMvc.perform(post("/user/{id}/lock", 4)
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.USER_VICTOR))))
                .andExpect(status().isForbidden());

        User expected = userRepository.getOne(4L);
        assertFalse(expected.isLocked());
    }

    @Test
    @Transactional
    void lockUserByLittleUserTest() throws Exception {
        mockMvc.perform(post("/user/{id}/lock", 4)
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsAdapter(UserData.USER_NASTYA))))
                .andExpect(status().isForbidden());

        User expected = userRepository.getOne(4L);
        assertFalse(expected.isLocked());
    }
}
