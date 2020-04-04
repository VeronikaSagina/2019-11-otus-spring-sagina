package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Assertions;
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

@SpringBootTest
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    void getListUsers() {
        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(UserData.USER));
        List<User> actual = userService.getListUsers();
        Assertions.assertEquals(List.of(UserData.USER), actual);
    }

    @Test
    void lockUser() {
        User unlocked = UserData.newUser(false);
        User locked = UserData.newUser(true);
        Mockito.when(userRepository.findById(unlocked.getId()))
                .thenReturn(Optional.of(unlocked));
        Mockito.when(userRepository.save(locked))
                .thenReturn(locked);
        userService.lockUser(unlocked.getId());
        Mockito.verify(userRepository).save(locked);
    }

    @Test
    void deleteUser() {
        Mockito.when(userRepository.findById(UserData.USER.getId()))
                .thenReturn(Optional.of(UserData.USER));
        userService.deleteUser(UserData.USER.getId());
        Mockito.verify(userRepository).deleteById(UserData.USER.getId());
    }
}