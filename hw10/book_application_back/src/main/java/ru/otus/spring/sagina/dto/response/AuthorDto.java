package ru.otus.spring.sagina.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorDto {
    @JsonProperty("id")
    public final String id;
    @JsonProperty("name")
    public final String name;

    public AuthorDto(@JsonProperty("id")String id,
                     @JsonProperty("name")String name) {
        this.id = id;
        this.name = name;
    }
}
