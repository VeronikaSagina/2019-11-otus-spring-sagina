package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.entity.User;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public List<User> getListUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User lockUser(Long userId) {
        User user = getUser(userId);
        user.setLocked(!user.isLocked());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        getUser(userId);
        userRepository.deleteById(userId);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("пользователь не найден"));
    }
}
