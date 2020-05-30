package ru.otus.spring.sagina.dto.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dto.request.CreateUserDto;
import ru.otus.spring.sagina.dto.response.UserDto;
import ru.otus.spring.sagina.entity.User;
import ru.otus.spring.sagina.enums.UserRole;

import java.util.List;

@Service
public class UserDtoMapper {
    private final PasswordEncoder passwordEncoder;

    public UserDtoMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.isLocked(),
                user.getRole());
    }

    public User toEntity(CreateUserDto createUserDto) {
        return new User(null,
                createUserDto.getLogin(),
                createUserDto.getEmail(),
                false,
                passwordEncoder.encode(createUserDto.getPassword()),
                createUserDto.isConsentToCommunication(),
                UserRole.ROLE_USER, List.of());
    }
}
