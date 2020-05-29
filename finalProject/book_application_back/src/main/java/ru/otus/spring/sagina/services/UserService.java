package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.sagina.dto.mapper.UserDtoMapper;
import ru.otus.spring.sagina.dto.request.CreateUserDto;
import ru.otus.spring.sagina.dto.response.UserDto;
import ru.otus.spring.sagina.entity.User;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    public UserService(UserRepository userRepository, UserDtoMapper userDtoMapper) {
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
    }

    @Transactional
    public List<User> getListUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User lockUser(UUID userId) {
        User user = getUser(userId);
        user.setLocked(!user.isLocked());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        getUser(userId);
        userRepository.deleteById(userId);
    }

    @Transactional
    public User create(CreateUserDto authUserDto) {
        return userRepository.save(userDtoMapper.toEntity(authUserDto));
    }

    @Transactional
    public User update(UserDto userDto) {
        User user = getUser(userDto.getId());
        Optional.ofNullable(userDto.getLocked())
                .ifPresent(user::setLocked);
        Optional.ofNullable(userDto.getLogin())
                .ifPresent(user::setLogin);
        Optional.ofNullable(userDto.getEmail())
                .ifPresent(user::setEmail);
        Optional.ofNullable(userDto.getRole())
                .ifPresent(user::setRole);
        return userRepository.save(user);
    }

    public Optional<User> findUserByLogin(String login) {
        return userRepository.findByLoginIgnoreCase(login);
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("пользователь не найден"));
    }
}
