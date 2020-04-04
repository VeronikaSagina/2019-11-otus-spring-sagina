package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.entity.User;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getListUsers() {
        return userRepository.findAll();
    }

    public void lockUser(String userId) {
        User user = getUser(userId);
        user.setLocked(!user.isLocked());
        userRepository.save(user);
    }

    public void deleteUser(String userId) {
        getUser(userId);
        userRepository.deleteById(userId);
    }

    private User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("пользователь не найден"));
    }
}
