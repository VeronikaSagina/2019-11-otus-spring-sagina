package ru.otus.spring.sagina.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailDto {
    @JsonProperty("email")
    public final String email;
    @JsonProperty("name")
    public final String name;
    @JsonProperty("bookInfo")
    public final BookInfoDto bookInfo;

    public EmailDto(@JsonProperty("email") String email,
                    @JsonProperty("name") String name,
                    @JsonProperty("bookInfo") BookInfoDto bookInfo) {
        this.email = email;
        this.name = name;
        this.bookInfo = bookInfo;
    }
}
