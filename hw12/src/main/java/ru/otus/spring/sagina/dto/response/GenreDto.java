package ru.otus.spring.sagina.dto.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class GenreDto {
    private String id;
    private String type;
}
