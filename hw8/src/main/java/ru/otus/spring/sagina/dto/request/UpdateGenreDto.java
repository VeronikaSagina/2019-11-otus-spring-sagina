package ru.otus.spring.sagina.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UpdateGenreDto {
    @NotNull
    @JsonProperty("id")
    public final String id;
    @NotBlank
    @JsonProperty("type")
    public final String type;

    public UpdateGenreDto(@JsonProperty("id") String id,
                          @JsonProperty("type") String type) {
        this.id = id;
        this.type = type;
    }
}
