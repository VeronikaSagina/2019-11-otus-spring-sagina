package ru.otus.spring.sagina.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class CreateGenreDto {
    @NotBlank
    @JsonProperty("type")
    public final String type;

    public CreateGenreDto(@JsonProperty("type") String type) {
        this.type = type;
    }
}
