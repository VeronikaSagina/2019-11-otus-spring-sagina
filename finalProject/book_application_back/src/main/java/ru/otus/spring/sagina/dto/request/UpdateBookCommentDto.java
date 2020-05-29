package ru.otus.spring.sagina.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.otus.spring.sagina.dto.response.ShortUserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookCommentDto {
    @NotNull
    @JsonProperty("id")
    private UUID id;
    @NotNull
    @JsonProperty("bookId")
    private UUID bookId;
    @NotBlank
    @JsonProperty("message")
    private String message;
    @NotNull
    @JsonProperty("user")
    private ShortUserDto shortUserDto;
}
