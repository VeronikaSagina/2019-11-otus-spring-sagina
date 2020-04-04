package ru.otus.spring.sagina.dto.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.otus.spring.sagina.enums.UserRole;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class UserDto {
    private String id;
    private String login;
    private UserRole role;
    private boolean isLocked;
}
