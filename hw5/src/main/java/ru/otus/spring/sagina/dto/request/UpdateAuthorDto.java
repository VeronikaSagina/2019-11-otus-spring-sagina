package ru.otus.spring.sagina.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UpdateAuthorDto {
    @NotNull
    @JsonProperty("id")
    public final Integer id;
    @NotBlank
    @JsonProperty("name")
    public final String name;

    public UpdateAuthorDto(@JsonProperty("id") Integer id,
                           @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }
}
