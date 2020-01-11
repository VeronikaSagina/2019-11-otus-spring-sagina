package ru.otus.spring.sagina.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Book {
    private final int id;
    private final String title;
    private final Author author;
    private Set<Genre> genres = new HashSet<>();
}
