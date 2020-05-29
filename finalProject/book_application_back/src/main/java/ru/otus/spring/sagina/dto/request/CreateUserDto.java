package ru.otus.spring.sagina.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@ToString
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class CreateUserDto {
    @NotBlank
    @JsonProperty("login")
    private String login;
    @NotBlank
    @JsonProperty("email")
    private String email;
    @NotBlank
    @JsonProperty("password")
    private String password;
    @JsonProperty("consentToCommunication")
    private boolean consentToCommunication = true;
}
