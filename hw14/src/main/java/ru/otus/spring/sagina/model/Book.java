package ru.otus.spring.sagina.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String id;
    private String name;
    private String description;
    private Author author;
    private List<Genre> genres = new ArrayList<>();
}
