package ru.otus.spring.sagina.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class BookCommentDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("message")
    private String message;
    @JsonProperty("bookId")
    private Long bookId;
}
