package ru.otus.spring.sagina.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.exceptions.AuthenticationException;
import ru.otus.spring.sagina.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLoginIgnoreCase(login)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new AuthenticationException("пользователь " + login + " не найден"));
    }
}