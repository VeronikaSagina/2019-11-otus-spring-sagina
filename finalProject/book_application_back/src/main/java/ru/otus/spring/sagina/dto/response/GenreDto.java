package ru.otus.spring.sagina.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class GenreDto {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("name")
    private String name;
}
