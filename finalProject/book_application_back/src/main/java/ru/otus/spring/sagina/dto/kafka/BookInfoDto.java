package ru.otus.spring.sagina.dto.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class BookInfoDto {
    @JsonProperty("id")
    public final UUID id;
    @JsonProperty("name")
    public final String name;
    @JsonProperty("content")
    public final String content;

    public BookInfoDto(
            @JsonProperty("id") UUID id,
                    @JsonProperty("name") String name,
                    @JsonProperty("content") String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }
}
