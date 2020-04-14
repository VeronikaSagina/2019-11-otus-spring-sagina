package ru.otus.spring.sagina.testdata;

import ru.otus.spring.sagina.entity.Book;

import java.util.List;

public class BookData {
    public static final Book ANNA_KARENINA =
            new Book(1L, "Анна Каренина",
                    "«Анну Каренину» Толстой называл «романом широким и свободным»."
                            + " В основе этого определения пушкинский термин «свободный роман»."
                            + " Не фабульная завершенность положений, а творческая концепция определяет"
                            + " выбор материала и открывает простор для развития сюжетных линий."
                            + " Роман «широкого дыхания» привлекал Толстого тем, что в «просторную,"
                            + " вместительную раму» без напряжения входило все то новое,"
                            + " необычайное и нужное, что он хотел сказать людям.",
                    AuthorData.TOLSTOY, List.of(GenreData.NOVEL), List.of());
    public static final Book LORD_OF_THE_RINGS =
            new Book(4L, "Властелин колец", "ОПИСАНИЕ", AuthorData.TOLKIEN, List.of(), List.of());
    public static final Book SNUFF =
            new Book(7L, "SNAFF", "ОПИСАНИЕ", AuthorData.PELEVIN, List.of(), List.of());
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
