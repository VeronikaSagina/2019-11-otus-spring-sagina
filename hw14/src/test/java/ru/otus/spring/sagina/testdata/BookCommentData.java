package ru.otus.spring.sagina.testdata;

import ru.otus.spring.sagina.model.BookComment;

import java.util.List;

public class BookCommentData {

    public static List<BookComment> getCommentsByBookId(int id) {
        switch (id) {
            case 1:
                return List.of(
                        new BookComment("1", "очень хорошая книга стоит почитать", BookData.BOOK_1),
                        new BookComment("2", "this is my favorite russian book!", BookData.BOOK_1));
            case 2:
                return List.of();
            case 3:
                return List.of(new BookComment("3", "не тратьте время", BookData.BOOK_3));
            case 4:
                return List.of(new BookComment("4", "фильм лучше!", BookData.BOOK_4),
                        new BookComment("5", "прочитала за вечер, рекомендую", BookData.BOOK_4));
            default:
                throw new RuntimeException("нет книги с таким id");
        }
    }
}
