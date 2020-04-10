package ru.otus.spring.sagina.dto.mapper;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dto.response.UserDto;
import ru.otus.spring.sagina.entity.User;

@Service
public class UserDtoMapper {
    public UserDto toDto(User user){
        return new UserDto(user.getId(), user.getLogin(), user.getRole(), user.isLocked());
    }
}
