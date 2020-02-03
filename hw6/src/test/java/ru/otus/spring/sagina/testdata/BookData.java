package ru.otus.spring.sagina.testdata;

import ru.otus.spring.sagina.domain.Book;

public class BookData {
    public static final Book ANNA_KARENINA = new Book(1, "Анна Каренина", AuthorData.TOLSTOY);
    public static final Book BLOOD_OF_ELVES = new Book(2, "Кровь эльфов", AuthorData.SAPKOWSKI);
    public static final Book ORIENT_EXPRESS = new Book(3, "Убийство в Восточном экспрессе", AuthorData.CHRISTIE);
    public static final Book LORD_OF_THE_RINGS = new Book(4, "Властелин колец", AuthorData.TOLKIEN);
    public static final Book SNUFF = new Book(5, "SNAFF", AuthorData.PELEVIN);

    public static Book getBookForAnnaKarenina() {
        return new Book(ANNA_KARENINA.getId(), ANNA_KARENINA.getTitle(), ANNA_KARENINA.getAuthor());
    }

    public static Book getBookForBloodOfElves() {
        return new Book(BLOOD_OF_ELVES.getId(), BLOOD_OF_ELVES.getTitle(), BLOOD_OF_ELVES.getAuthor());
    }

    public static Book getBookForLordOfTheRing() {
        return new Book(LORD_OF_THE_RINGS.getId(), LORD_OF_THE_RINGS.getTitle(), LORD_OF_THE_RINGS.getAuthor());
    }

    public static Book getBookForSnuff() {
        return new Book(SNUFF.getId(), SNUFF.getTitle(), SNUFF.getAuthor());
    }
}
