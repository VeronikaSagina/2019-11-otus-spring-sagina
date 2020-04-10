package ru.otus.spring.sagina.testdata;

import ru.otus.spring.sagina.entity.Genre;


public class GenreData {
    public static final Genre DETECTIVE = new Genre(2L, "детектив");
    public static final Genre KIDS_BOOK_GENRE = new Genre(5L, "литература для детей");
    public static final Genre NOVEL = new Genre(1L, "роман");
    public static final Genre FANTASTIC = new Genre(4L, "фантастика");
    public static final Genre FANTASY = new Genre(3L, "фентези");
}
