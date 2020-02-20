package ru.otus.spring.sagina.testdata;

import ru.otus.spring.sagina.entity.Book;

import java.util.List;

public class BookData {
    public static final Book ANNA_KARENINA = new Book("1", "Анна Каренина", AuthorData.TOLSTOY, List.of(), List.of());
    public static final Book BLOOD_OF_ELVES = new Book("2", "Кровь эльфов", AuthorData.SAPKOWSKI, List.of(), List.of());
    public static final Book ORIENT_EXPRESS = new Book("3", "Убийство в Восточном экспрессе", AuthorData.CHRISTIE, List.of(), List.of());
    public static final Book LORD_OF_THE_RINGS = new Book("4", "Властелин колец", AuthorData.TOLKIEN, List.of(), List.of());
    public static final Book SNUFF = new Book("5", "SNAFF", AuthorData.PELEVIN, List.of(), List.of());

    public static Book getBookForAnnaKarenina() {
        Book book = new Book();
        book.setId(ANNA_KARENINA.getId());
        book.setTitle(ANNA_KARENINA.getTitle());
        book.setAuthor(ANNA_KARENINA.getAuthor());
        return book;
    }

    public static Book getBookForBloodOfElves() {
        Book book =  new Book();
        book.setId(BLOOD_OF_ELVES.getId());
        book.setTitle(BLOOD_OF_ELVES.getTitle());
        book.setAuthor(BLOOD_OF_ELVES.getAuthor());
        return book;
    }

    public static Book getBookForLordOfTheRing() {
        Book book =  new Book();
        book.setId(LORD_OF_THE_RINGS.getId());
        book.setTitle(LORD_OF_THE_RINGS.getTitle());
        book.setAuthor(LORD_OF_THE_RINGS.getAuthor());
        return book;
    }

    public static Book getBookForSnuff() {
        Book book =  new Book();
        book.setId(SNUFF.getId());
        book.setTitle(SNUFF.getTitle());
        book.setAuthor(SNUFF.getAuthor());
        return book;
    }
}
