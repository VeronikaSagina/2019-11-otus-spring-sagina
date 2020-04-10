package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.spring.sagina.BookApplication;
import ru.otus.spring.sagina.dto.request.BookDtoRequest;
import ru.otus.spring.sagina.dto.response.AuthorDto;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.security.UserDetailsAdapter;
import ru.otus.spring.sagina.testdata.AuthorData;
import ru.otus.spring.sagina.testdata.BookData;
import ru.otus.spring.sagina.testdata.GenreData;
import ru.otus.spring.sagina.testdata.UserData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = BookApplication.class)
class BookServiceTest {
    @Autowired
    private BookService bookService;

    @Test
    void createBookTest() {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        new UserDetailsAdapter(UserData.ADMIN_VERONIKA),
                        UserData.ADMIN_VERONIKA.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));

        BookDtoRequest created = new BookDtoRequest(null, BookData.SNUFF.getName(), BookData.DESCRIPTION,
                new AuthorDto(AuthorData.PELEVIN.getId(), AuthorData.PELEVIN.getName()),
                List.of(GenreData.FANTASTIC.getId(), GenreData.NOVEL.getId()));

        Book actual = bookService.createBook(created);

        assertEquals(AuthorData.PELEVIN, actual.getAuthor());
        assertEquals(List.of(GenreData.NOVEL, GenreData.FANTASTIC), actual.getGenres());
        assertEquals(created.getName(), actual.getName());
        assertEquals(created.getDescription(), actual.getDescription());
    }

    @Test
    void updateBookTest() {
        BookDtoRequest updateBook = new BookDtoRequest(1L,
                BookData.ANNA_KARENINA.getName(),
                BookData.DESCRIPTION,
                new AuthorDto(AuthorData.TOLSTOY.getId(), AuthorData.TOLSTOY.getName()),
                List.of(1L, 2L));
        Book expected = new Book(updateBook.getId(),
                updateBook.getName(),
                updateBook.getDescription(),
                AuthorData.TOLSTOY,
                List.of(GenreData.NOVEL, GenreData.DETECTIVE), List.of());

        assertEquals(expected, bookService.updateBook(updateBook));
    }

    @Test
    void updateBookExceptionTest() {
        assertThrows(NotFoundException.class,
                () -> bookService.updateBook(new BookDtoRequest(
                        100L, null, null, new AuthorDto(1L, ""), List.of(1L, 2L))));
    }

    @Test
    public void deleteBookTest() {
        bookService.deleteBook(1L);
    }

    @Test
    void getAllBooksTest() {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        UserData.ADMIN_VERONIKA.getLogin(),
                        UserData.ADMIN_VERONIKA.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
        List<Book> actual = bookService.getAllBooks();
        assertEquals(6, actual.size());
        SecurityContextHolder.clearContext();
    }

    @Test
    void getBookTest() {
        Book actual = bookService.getBook(1L);
        assertEquals(BookData.ANNA_KARENINA.getId(), actual.getId());
        assertEquals(BookData.ANNA_KARENINA.getName(), actual.getName());
        assertEquals(BookData.ANNA_KARENINA.getAuthor().getId(), actual.getAuthor().getId());
        assertEquals(BookData.ANNA_KARENINA.getAuthor().getName(), actual.getAuthor().getName());
    }
}
