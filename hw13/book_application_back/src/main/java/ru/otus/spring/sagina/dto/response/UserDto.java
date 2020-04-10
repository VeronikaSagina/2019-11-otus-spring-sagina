package ru.otus.spring.sagina.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.otus.spring.sagina.enums.UserRole;

@ToString
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class UserDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("login")
    private String login;
    @JsonProperty("role")
    private UserRole role;
    @JsonProperty("locked")
    private boolean locked;
}
