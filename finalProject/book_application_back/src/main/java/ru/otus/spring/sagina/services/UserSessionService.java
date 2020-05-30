package ru.otus.spring.sagina.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.entity.User;
import ru.otus.spring.sagina.repository.UserRepository;
import ru.otus.spring.sagina.security.AuthUser;

@Service
public class UserSessionService {
private final UserRepository userRepository;

    public UserSessionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUserProxy() {
        Object principal = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        var authUser = (AuthUser) principal;
        return userRepository.getOne(authUser.getId());
    }
}
