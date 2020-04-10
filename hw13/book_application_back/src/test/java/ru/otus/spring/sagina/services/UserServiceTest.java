package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.sagina.entity.User;
import ru.otus.spring.sagina.repository.UserRepository;
import ru.otus.spring.sagina.testdata.UserData;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    void getListUsersTest() {
        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(UserData.ADMIN_VASYA, UserData.USER_NASTYA));
        List<User> actual = userService.getListUsers();
        assertEquals(List.of(UserData.ADMIN_VASYA, UserData.USER_NASTYA), actual);
    }

    @Test
    void lockUserTest() {
        Mockito.when(userRepository.findById(3L))
                .thenReturn(Optional.of(UserData.USER_NASTYA));
        User expected = UserData.newUserNastya(true);
        Mockito.when(userRepository.save(expected))
                .thenReturn(expected);
        User actual = userService.lockUser(3L);
        Mockito.verify(userRepository).save(expected);
        assertEquals(expected, actual);
    }

    @Test
    void deleteUserTest() {
        Mockito.when(userRepository.findById(3L))
                .thenReturn(Optional.of(UserData.USER_NASTYA));
        userService.deleteUser(3L);
        Mockito.verify(userRepository).deleteById(3L);
    }
}