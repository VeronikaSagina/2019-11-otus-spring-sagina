package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.sagina.dto.request.CreateBookDto;
import ru.otus.spring.sagina.dto.request.UpdateBookDto;
import ru.otus.spring.sagina.entity.Author;
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
import java.util.stream.Collectors;

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
    void getBookByTitle() {
        Mockito.when(bookRepository.findAllByTitleContainingIgnoreCaseOrderByTitle("эльф"))
                .thenReturn(List.of(BookData.getBookForBloodOfElves()));
        List<Book> actual = bookService.getBookByTitle("эльф");
        Mockito.verify(bookRepository).findAllByTitleContainingIgnoreCaseOrderByTitle("эльф");
        Book book = actual.get(0);
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(BookData.BLOOD_OF_ELVES.getId(), book.getId());
        Assertions.assertEquals(BookData.BLOOD_OF_ELVES.getAuthor().getId(), book.getAuthor().getId());
        Assertions.assertEquals(BookData.BLOOD_OF_ELVES.getTitle(), book.getTitle());
    }

    @Test
    void getAllBooks() {
        Mockito.when(bookRepository.findAll()).thenReturn(List.of(
                BookData.getBookForAnnaKarenina(), BookData.getBookForLordOfTheRing()));
        List<Book> actual = bookService.getAllBooks();
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(1, actual.get(0).getId());
        Assertions.assertEquals(4, actual.get(1).getId());

    }

    @Test
    void getAllBooksByAuthorName() {
        Mockito.when(authorRepository.findAllByNameContainingIgnoreCaseOrderByName("п"))
                .thenReturn(List.of(AuthorData.PELEVIN, AuthorData.SAPKOWSKI));

        Mockito.when(bookRepository
                .findAllByAuthorIdIn(List.of(AuthorData.PELEVIN.getId(), AuthorData.SAPKOWSKI.getId())))
                .thenReturn(List.of(BookData.getBookForBloodOfElves(), BookData.getBookForSnuff()));

        List<Book> actual = bookService.getAllBooksByAuthorName("п");
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(AuthorData.PELEVIN.getId(), actual.get(0).getAuthor().getId());
        Assertions.assertEquals(AuthorData.PELEVIN.getName(), actual.get(0).getAuthor().getName());
        Assertions.assertEquals(AuthorData.SAPKOWSKI.getId(), actual.get(1).getAuthor().getId());
        Assertions.assertEquals(AuthorData.SAPKOWSKI.getName(), actual.get(1).getAuthor().getName());
    }

    @Test
    void getBooksByGenreId() {
        Mockito.when(bookRepository.findAllByGenreId(1))
                .thenReturn(List.of(BookData.getBookForAnnaKarenina(), BookData.getBookForLordOfTheRing()));
        List<Book> actual = bookService.getBooksByGenreId(1);
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getId(), actual.get(0).getId());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getTitle(), actual.get(0).getTitle());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getId(), actual.get(0).getAuthor().getId());

        Assertions.assertEquals(BookData.LORD_OF_THE_RINGS.getId(), actual.get(1).getId());
        Assertions.assertEquals(BookData.LORD_OF_THE_RINGS.getTitle(), actual.get(1).getTitle());
        Assertions.assertEquals(BookData.LORD_OF_THE_RINGS.getAuthor().getId(), actual.get(1).getAuthor().getId());
    }

    @Test
    void createBook() {
        CreateBookDto created = new CreateBookDto(BookData.SNUFF.getTitle(), AuthorData.PELEVIN.getId(),
                List.of(GenreData.FANTASTIC.getId(), GenreData.NOVEL.getId()));
        Mockito.when(authorRepository.findById(AuthorData.PELEVIN.getId())).thenReturn(Optional.of(AuthorData.PELEVIN));
        Mockito.when(genreRepository.findAllByIdIn(created.genreIds)).thenReturn(List.of(GenreData.FANTASTIC, GenreData.NOVEL));
        Mockito.when(bookRepository.save(Mockito.any())).thenReturn(BookData.SNUFF);

        Book expected = new Book();
        expected.setTitle(created.title);
        expected.setAuthor(AuthorData.PELEVIN);
        expected.getGenres().addAll(List.of(GenreData.FANTASTIC, GenreData.NOVEL));

        Book actual = bookService.createBook(created);
        Mockito.verify(bookRepository).save(expected);

        Assertions.assertEquals(BookData.SNUFF.getId(), actual.getId());
        Assertions.assertEquals(BookData.SNUFF.getAuthor().getId(), actual.getAuthor().getId());
        Assertions.assertEquals(BookData.SNUFF.getAuthor().getName(), actual.getAuthor().getName());
        Assertions.assertEquals(BookData.SNUFF.getTitle(), actual.getTitle());
    }

    @Test
    void updateBook() {
        UpdateBookDto updateBook = new UpdateBookDto(1, BookData.ANNA_KARENINA.getTitle(), null, List.of(1, 2));
        Mockito.when(bookRepository.findById(updateBook.id)).thenReturn(Optional.of(BookData.ANNA_KARENINA));
        Mockito.when(genreRepository.findAllByIdIn(updateBook.genreIds))
                .thenReturn(List.of(GenreData.NOVEL, GenreData.DETECTIVE));
        Book book = BookData.getBookForAnnaKarenina();
        book.setGenres(List.of(GenreData.NOVEL, GenreData.DETECTIVE));
        Mockito.when(bookRepository.save(Mockito.any())).thenReturn(book);
        Book actual = bookService.updateBook(updateBook);
        book.setGenres(List.of(GenreData.NOVEL, GenreData.DETECTIVE));

        Assertions.assertEquals(BookData.ANNA_KARENINA.getId(), actual.getId());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getTitle(), actual.getTitle());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getName(), actual.getAuthor().getName());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getId(), actual.getAuthor().getId());
        Assertions.assertEquals(2, actual.getGenres().size());
        Assertions.assertTrue(actual.getGenres().containsAll(Set.of(GenreData.NOVEL, GenreData.DETECTIVE)));
    }

    @Test
    void updateBookExceptionTest() {
        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class,
                () -> bookService.updateBook(new UpdateBookDto(1, null, null, List.of(1, 2))));
    }

    @Test
    void deleteBooksByAuthorId() {
        List<Book> books = List.of(BookData.ANNA_KARENINA, BookData.BLOOD_OF_ELVES);
        Author author = new Author(AuthorData.TOLSTOY.getId(), AuthorData.TOLSTOY.getName(), books);
        Mockito.when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        bookService.deleteBooksByAuthorId(1);
        Mockito.verify(commentRepository).deleteAllByBookIdIn(books.stream()
                .map(Book::getId)
                .collect(Collectors.toList()));
        Mockito.verify(bookRepository).deleteAll(books);
    }

    @Test
    void getBook() {
        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.of(BookData.getBookForAnnaKarenina()));
        Book actual = bookService.getBook(1);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getId(), actual.getId());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getTitle(), actual.getTitle());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getId(), actual.getAuthor().getId());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getName(), actual.getAuthor().getName());
    }

    @Test
    void getBookWithComments() {
        Book book = BookData.getBookForAnnaKarenina();
        book.getComments().add(BookCommentData.KARENINA_COMMENT_1);
        book.getComments().add(BookCommentData.KARENINA_COMMENT_2);
        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        Book actual = bookService.getBookWithComments(1);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getId(), actual.getId());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getTitle(), actual.getTitle());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getId(), actual.getAuthor().getId());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getName(), actual.getAuthor().getName());
        Assertions.assertEquals(BookCommentData.KARENINA_COMMENT_1.getId(), actual.getComments().get(0).getId());
        Assertions.assertEquals(BookCommentData.KARENINA_COMMENT_2.getId(), actual.getComments().get(1).getId());
    }
}
