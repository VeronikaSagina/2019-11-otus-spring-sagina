package ru.otus.spring.sagina.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Author {
    private final int id;
    private final String name;
    private Set<Book> books = new HashSet<>();
}
