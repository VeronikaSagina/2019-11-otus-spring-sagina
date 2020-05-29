package ru.otus.spring.sagina.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.otus.spring.sagina.annotation.NullOrNotBlank;
import ru.otus.spring.sagina.enums.UserRole;

import java.util.UUID;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class UserDto {
    @NullOrNotBlank
    @JsonProperty("id")
    private UUID id;

    @NullOrNotBlank
    @JsonProperty("login")
    private String login;

    @NullOrNotBlank
    @JsonProperty("email")
    private String email;

    @NullOrNotBlank
    @JsonProperty("locked")
    private Boolean locked;

    @NullOrNotBlank
    @JsonProperty("role")
    private UserRole role;
}
