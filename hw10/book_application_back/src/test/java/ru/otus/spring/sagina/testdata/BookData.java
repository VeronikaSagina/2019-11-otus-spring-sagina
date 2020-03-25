package ru.otus.spring.sagina.testdata;

import ru.otus.spring.sagina.entity.Book;

import java.util.List;

public class BookData {
    public static final Book ANNA_KARENINA =
            new Book("1", "Анна Каренина", "ОПИСАНИЕ", AuthorData.TOLSTOY, List.of(), List.of());
    public static final Book LORD_OF_THE_RINGS =
            new Book("4", "Властелин колец", "ОПИСАНИЕ", AuthorData.TOLKIEN, List.of(), List.of());
    public static final Book SNUFF =
            new Book("5", "SNAFF", "ОПИСАНИЕ", AuthorData.PELEVIN, List.of(), List.of());
    public static final String DESCRIPTION = "ОПИСАНИЕ";

    public static Book getBookForAnnaKarenina() {
        Book book = new Book();
        book.setId(ANNA_KARENINA.getId());
        book.setName(ANNA_KARENINA.getName());
        book.setAuthor(ANNA_KARENINA.getAuthor());
        book.setDescription(ANNA_KARENINA.getDescription());
        return book;
    }

    public static Book getBookForLordOfTheRing() {
        Book book = new Book();
        book.setId(LORD_OF_THE_RINGS.getId());
        book.setName(LORD_OF_THE_RINGS.getName());
        book.setAuthor(LORD_OF_THE_RINGS.getAuthor());
        book.setDescription(LORD_OF_THE_RINGS.getDescription());
        return book;
    }
}
