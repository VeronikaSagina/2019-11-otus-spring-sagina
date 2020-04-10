package ru.otus.spring.sagina.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookCommentDtoRequest {
    @JsonProperty("id")
    private String id;
    @NotNull
    @JsonProperty("bookId")
    private Long bookId;
    @NotBlank
    @JsonProperty("message")
    private String message;
}
