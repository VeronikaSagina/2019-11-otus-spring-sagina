package ru.otus.spring.sagina.testdata;

import ru.otus.spring.sagina.entity.Author;

import java.util.List;

public class AuthorData {
    public static final Author TOLSTOY = new Author(1, "Лев Толстой", List.of());
    public static final Author SAPKOWSKI = new Author(2, "Анджей Сапко́вский", List.of());
    public static final Author CHRISTIE = new Author(3, "Агата Кристи", List.of());
    public static final Author TOLKIEN = new Author(4, "Джон Толкин", List.of());
    public static final Author PELEVIN = new Author(5, "Виктор Пелевин", List.of());
    public static final Author PUSHKIN = new Author(6, "Александр Пушкин", List.of());
}
