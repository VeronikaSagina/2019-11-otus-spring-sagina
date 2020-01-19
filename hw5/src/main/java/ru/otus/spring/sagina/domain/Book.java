package ru.otus.spring.sagina.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private int id;
    private String title;
    private Author author;
    private Set<Genre> genres = new HashSet<>();

    public Book(int id, String title, Author author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public Book(String title, Author author) {
        this.title = title;
        this.author = author;
    }
}
