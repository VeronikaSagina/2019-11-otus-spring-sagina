package ru.otus.spring.sagina.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class CreateAuthorDto {
    @NotBlank
    @JsonProperty("name")
    public final String name;

    public CreateAuthorDto(@JsonProperty("name") String name) {
        this.name = name;
    }
}
