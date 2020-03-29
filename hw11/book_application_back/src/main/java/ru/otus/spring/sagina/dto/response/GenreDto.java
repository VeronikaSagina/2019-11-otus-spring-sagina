package ru.otus.spring.sagina.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class GenreDto {
    @JsonProperty("id")
    public final String id;
    @JsonProperty("name")
    public final String name;

    public GenreDto(@JsonProperty("id") String id,
                    @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }
}
