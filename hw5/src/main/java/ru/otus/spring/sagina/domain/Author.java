package ru.otus.spring.sagina.domain;

import lombok.*;

@ToString
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    private int id;
    private String name;

    public Author(String name) {
        this.name = name;
    }
}
