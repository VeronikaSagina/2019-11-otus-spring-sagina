package ru.otus.spring.sagina.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    private int id;
    private String type;

    public Genre(String type) {
        this.type = type;
    }
}
