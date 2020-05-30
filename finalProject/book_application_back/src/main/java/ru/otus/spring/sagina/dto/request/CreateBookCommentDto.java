package ru.otus.spring.sagina.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookCommentDto {
    @NotNull
    @JsonProperty("bookId")
    private UUID bookId;
    @NotBlank
    @JsonProperty("message")
    private String message;
}
