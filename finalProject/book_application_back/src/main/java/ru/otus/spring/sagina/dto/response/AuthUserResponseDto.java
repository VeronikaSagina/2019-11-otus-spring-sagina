package ru.otus.spring.sagina.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.spring.sagina.enums.UserRole;

@Getter
@AllArgsConstructor
public class AuthUserResponseDto {
    @JsonProperty("jwt")
    private String jwt;
    @JsonProperty("role")
    private UserRole role;
}
