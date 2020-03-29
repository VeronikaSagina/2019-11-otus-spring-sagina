package ru.otus.spring.sagina.testdata;

import ru.otus.spring.sagina.dto.mapper.AuthorDtoMapper;
import ru.otus.spring.sagina.dto.request.BookDtoRequest;
import ru.otus.spring.sagina.dto.response.AuthorDto;
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.entity.Genre;

import java.util.List;

public class BookData {
    private static final String DESCRIPTION = "описание";
    public static final Book ANNA_KARENINA =
            new Book("1", "Анна Каренина", DESCRIPTION, AuthorData.TOLSTOY, List.of(), List.of());

    public static final Book LORD_OF_THE_RINGS =
            new Book("4", "Властелин колец", DESCRIPTION, AuthorData.TOLKIEN, List.of(), List.of());

    public static final Book BLOOD_OF_ELVES =
            new Book("2", "Кровь эльфов", DESCRIPTION, AuthorData.SAPKOWSKI, List.of(), List.of());

    public static final Book ORIENT_EXPRESS =
            new Book("3", "Убийство в \"Восточном экспрессе\"", DESCRIPTION, AuthorData.CHRISTIE, List.of(), List.of());

    public static final BookDtoRequest NEW_BOOK_DTO =
            new BookDtoRequest(null, "Война и мир", DESCRIPTION,
                    AuthorDtoMapper.toDto(AuthorData.TOLSTOY), List.of("1"));

    public static final Book NEW_BOOK = new Book(null, "Война и мир", DESCRIPTION,
            AuthorData.TOLSTOY, List.of(GenreData.NOVEL), List.of());

    public static final BookDtoRequest UPDATE_BOOK_DTO = new BookDtoRequest("1", "name", DESCRIPTION,
            new AuthorDto("1", "authorName"), List.of("1"));

    public static final Book UPDATED = new Book("1", "name", DESCRIPTION,
            new Author("1", "authorName"), List.of(new Genre("1", "genreName")), null);

    public static Book getBookForAnnaKarenina() {
        Book book = new Book();
        book.setId(ANNA_KARENINA.getId());
        book.setName(ANNA_KARENINA.getName());
        book.setAuthor(ANNA_KARENINA.getAuthor());
        book.setDescription(ANNA_KARENINA.getDescription());
        return book;
    }
}
