package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.sagina.dto.request.BookDtoRequest;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.repository.AuthorRepository;
import ru.otus.spring.sagina.repository.BookCommentRepository;
import ru.otus.spring.sagina.repository.BookRepository;
import ru.otus.spring.sagina.repository.GenreRepository;
import ru.otus.spring.sagina.testdata.AuthorData;
import ru.otus.spring.sagina.testdata.BookCommentData;
import ru.otus.spring.sagina.testdata.BookData;
import ru.otus.spring.sagina.testdata.GenreData;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookCommentRepository commentRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private GenreRepository genreRepository;
    @InjectMocks
    private BookService bookService;

    @Test
    void createBookTest() {
        BookDtoRequest created = new BookDtoRequest("", BookData.SNUFF.getTitle(), AuthorData.PELEVIN.getId(), BookData.DESCRIPTION,
                List.of(GenreData.FANTASTIC.getId(), GenreData.NOVEL.getId()));
        Mockito.when(authorRepository.findById(AuthorData.PELEVIN.getId())).thenReturn(Optional.of(AuthorData.PELEVIN));
        Mockito.when(genreRepository.findAllByIdIn(created.genreIds)).thenReturn(List.of(GenreData.FANTASTIC, GenreData.NOVEL));
        Mockito.when(bookRepository.save(Mockito.any())).thenReturn(BookData.SNUFF);

        Book expected = new Book();
        expected.setTitle(created.title);
        expected.setAuthor(AuthorData.PELEVIN);
        expected.setDescription(BookData.DESCRIPTION);
        expected.getGenres().addAll(List.of(GenreData.FANTASTIC, GenreData.NOVEL));

        Book actual = bookService.createBook(created);
        Mockito.verify(bookRepository).save(expected);

        Assertions.assertEquals(BookData.SNUFF.getId(), actual.getId());
        Assertions.assertEquals(BookData.SNUFF.getAuthor().getId(), actual.getAuthor().getId());
        Assertions.assertEquals(BookData.SNUFF.getAuthor().getName(), actual.getAuthor().getName());
        Assertions.assertEquals(BookData.SNUFF.getTitle(), actual.getTitle());
    }

    @Test
    void updateBookTest() {
        BookDtoRequest updateBook = new BookDtoRequest("1", BookData.ANNA_KARENINA.getTitle(),
                "1", BookData.DESCRIPTION, List.of("1", "2"));
        Mockito.when(bookRepository.findById(updateBook.id)).thenReturn(Optional.of(BookData.getBookForAnnaKarenina()));
        Mockito.when(authorRepository.findById("1")).thenReturn(Optional.of(AuthorData.TOLSTOY));
        Mockito.when(genreRepository.findAllByIdIn(updateBook.genreIds))
                .thenReturn(List.of(GenreData.NOVEL, GenreData.DETECTIVE));
        Book book = BookData.getBookForAnnaKarenina();
        book.setGenres(List.of(GenreData.NOVEL, GenreData.DETECTIVE));
        Mockito.when(bookRepository.save(Mockito.any())).thenReturn(book);
        Book actual = bookService.updateBook(updateBook);

        Assertions.assertEquals(BookData.ANNA_KARENINA.getId(), actual.getId());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getTitle(), actual.getTitle());
        Assertions.assertEquals(BookData.DESCRIPTION, actual.getDescription());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getName(), actual.getAuthor().getName());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getId(), actual.getAuthor().getId());
        Assertions.assertEquals(2, actual.getGenres().size());
        Assertions.assertTrue(actual.getGenres().containsAll(Set.of(GenreData.NOVEL, GenreData.DETECTIVE)));
    }

    @Test
    void updateBookExceptionTest() {
        Mockito.when(bookRepository.findById("1")).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class,
                () -> bookService.updateBook(new BookDtoRequest("1", null, null, null, List.of("1", "2"))));
    }

    @Test
    public void deleteBookTest() {
        Mockito.when(bookRepository.findById("1")).thenReturn(Optional.of(BookData.ANNA_KARENINA));
        bookService.deleteBook("1");
        Mockito.verify(bookRepository).deleteById("1");
    }

    @Test
    void getAllBooksTest() {
        Mockito.when(bookRepository.findAll()).thenReturn(List.of(
                BookData.getBookForAnnaKarenina(), BookData.getBookForLordOfTheRing()));
        List<Book> actual = bookService.getAllBooks();
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals("1", actual.get(0).getId());
        Assertions.assertEquals("4", actual.get(1).getId());
    }

    @Test
    void getBookWithCommentsTest() {
        Mockito.when(bookRepository.findById("1")).thenReturn(Optional.of(BookData.getBookForAnnaKarenina()));
        Mockito.when(commentRepository.findAllByBookId("1"))
                .thenReturn(List.of(BookCommentData.KARENINA_COMMENT_1, BookCommentData.KARENINA_COMMENT_2));
        Book actual = bookService.getBookWithComments("1");
        Assertions.assertEquals(BookData.ANNA_KARENINA.getId(), actual.getId());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getTitle(), actual.getTitle());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getId(), actual.getAuthor().getId());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getName(), actual.getAuthor().getName());
        Assertions.assertEquals(BookCommentData.KARENINA_COMMENT_1.getId(), actual.getComments().get(0).getId());
        Assertions.assertEquals(BookCommentData.KARENINA_COMMENT_2.getId(), actual.getComments().get(1).getId());
    }

    @Test
    void getBookTest() {
        Mockito.when(bookRepository.findById("1")).thenReturn(Optional.of(BookData.getBookForAnnaKarenina()));
        Book actual = bookService.getBook("1");
        Assertions.assertEquals(BookData.ANNA_KARENINA.getId(), actual.getId());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getTitle(), actual.getTitle());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getId(), actual.getAuthor().getId());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getName(), actual.getAuthor().getName());
    }
}
