package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.sagina.dao.AuthorDao;
import ru.otus.spring.sagina.dao.BookDao;
import ru.otus.spring.sagina.dao.GenreDao;
import ru.otus.spring.sagina.domain.Book;
import ru.otus.spring.sagina.dto.mapper.GenreDtoMapper;
import ru.otus.spring.sagina.dto.request.CreateBookDto;
import ru.otus.spring.sagina.dto.request.UpdateBookDto;
import ru.otus.spring.sagina.dto.response.BookDto;
import ru.otus.spring.sagina.dto.response.BookWithCommentsDto;
import ru.otus.spring.sagina.exceptions.NotFoundException;
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
    private BookDao bookDao;
    @Mock
    private AuthorDao authorDao;
    @Mock
    private GenreDao genreDao;
    @InjectMocks
    private BookService bookService;

    @Test
    void getBookByTitle() {
        Mockito.when(bookDao.getByTitle("эльф")).thenReturn(List.of(BookData.getBookForBloodOfElves()));
        List<BookDto> actual = bookService.getBookByTitle("эльф");
        Mockito.verify(bookDao).getByTitle("эльф");
        BookDto book = actual.get(0);
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(BookData.BLOOD_OF_ELVES.getId(), book.id);
        Assertions.assertEquals(BookData.BLOOD_OF_ELVES.getAuthor().getId(), book.author.id);
        Assertions.assertEquals(BookData.BLOOD_OF_ELVES.getTitle(), book.title);
    }

    @Test
    void getAllBooks() {
        Mockito.when(bookDao.getAll()).thenReturn(List.of(
                BookData.getBookForAnnaKarenina(), BookData.getBookForLordOfTheRing()));
        List<BookDto> actual = bookService.getAllBooks();
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(1, actual.get(0).id);
        Assertions.assertEquals(4, actual.get(1).id);

    }

    @Test
    void getAllBooksByAuthorName() {
        Mockito.when(authorDao.getByName("п"))
                .thenReturn(List.of(AuthorData.SAPKOWSKI, AuthorData.PELEVIN));

        Mockito.when(bookDao.getByAuthorId(AuthorData.SAPKOWSKI.getId()))
                .thenReturn(List.of(BookData.getBookForBloodOfElves()));
        Mockito.when(bookDao.getByAuthorId(AuthorData.PELEVIN.getId()))
                .thenReturn(List.of(BookData.getBookForSnuff()));

        List<BookDto> actual = bookService.getAllBooksByAuthorName("п");
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(AuthorData.PELEVIN.getId(), actual.get(0).author.id);
        Assertions.assertEquals(AuthorData.PELEVIN.getName(), actual.get(0).author.name);
        Assertions.assertEquals(AuthorData.SAPKOWSKI.getId(), actual.get(1).author.id);
        Assertions.assertEquals(AuthorData.SAPKOWSKI.getName(), actual.get(1).author.name);
    }

    @Test
    void getBooksByGenreId() {
        Mockito.when(bookDao.getByGenreId(1))
                .thenReturn(List.of(BookData.getBookForAnnaKarenina(), BookData.getBookForLordOfTheRing()));
        List<BookDto> actual = bookService.getBooksByGenreId(1);
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getId(), actual.get(0).id);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getTitle(), actual.get(0).title);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getId(), actual.get(0).author.id);

        Assertions.assertEquals(BookData.LORD_OF_THE_RINGS.getId(), actual.get(1).id);
        Assertions.assertEquals(BookData.LORD_OF_THE_RINGS.getTitle(), actual.get(1).title);
        Assertions.assertEquals(BookData.LORD_OF_THE_RINGS.getAuthor().getId(), actual.get(1).author.id);
    }

    @Test
    void createBook() {
        CreateBookDto bookDto = new CreateBookDto(BookData.SNUFF.getTitle(), AuthorData.PELEVIN.getId(),
                List.of(GenreData.FANTASTIC.getId(), GenreData.NOVEL.getId()));
        Mockito.when(authorDao.findById(AuthorData.PELEVIN.getId())).thenReturn(Optional.of(AuthorData.PELEVIN));
        Mockito.when(genreDao.getByIds(bookDto.genreIds)).thenReturn(List.of(GenreData.FANTASTIC, GenreData.NOVEL));
        Mockito.when(bookDao.save(Mockito.any())).thenReturn(BookData.SNUFF);

        Book expected = new Book(bookDto.title, AuthorData.PELEVIN);
        expected.getGenres().addAll(List.of(GenreData.FANTASTIC, GenreData.NOVEL));

        BookDto actual = bookService.createBook(bookDto);
        Mockito.verify(bookDao).save(expected);

        Assertions.assertEquals(BookData.SNUFF.getId(), actual.id);
        Assertions.assertEquals(BookData.SNUFF.getAuthor().getId(), actual.author.id);
        Assertions.assertEquals(BookData.SNUFF.getAuthor().getName(), actual.author.name);
        Assertions.assertEquals(BookData.SNUFF.getTitle(), actual.title);
    }

    @Test
    void updateBook() {
        UpdateBookDto updateBookDto = new UpdateBookDto(1, BookData.ANNA_KARENINA.getTitle(), null, List.of(1, 2));
        Mockito.when(bookDao.findById(updateBookDto.id)).thenReturn(Optional.of(BookData.ANNA_KARENINA));
        Mockito.when(genreDao.getByIds(updateBookDto.genreIds))
                .thenReturn(List.of(GenreData.NOVEL, GenreData.DETECTIVE));
        Book book = BookData.getBookForAnnaKarenina();
        book.getGenres().addAll(List.of(GenreData.NOVEL, GenreData.DETECTIVE));
        Mockito.when(bookDao.save(Mockito.any())).thenReturn(book);
        BookDto actual = bookService.updateBook(updateBookDto);
        book.getGenres().addAll(List.of(GenreData.NOVEL, GenreData.DETECTIVE));

        Assertions.assertEquals(BookData.ANNA_KARENINA.getId(), actual.id);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getTitle(), actual.title);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getName(), actual.author.name);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getId(), actual.author.id);
        Assertions.assertEquals(2, actual.genres.size());
        Assertions.assertTrue(actual.genres.containsAll(
                Set.of(GenreDtoMapper.toDto(GenreData.NOVEL), GenreDtoMapper.toDto(GenreData.DETECTIVE))));
    }

    @Test
    void updateBookExceptionTest() {
        Mockito.when(bookDao.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class,
                () -> bookService.updateBook(new UpdateBookDto(1, null, null, List.of(1, 2))));
    }

    @Test
    void deleteBooks() {
        List<Book> books = List.of(BookData.ANNA_KARENINA, BookData.BLOOD_OF_ELVES);
        Mockito.when(bookDao.getByIds(List.of(1, 2))).thenReturn(books);
        bookService.deleteBooks(List.of(1, 2));
        Mockito.verify(bookDao).delete(books.get(0));
        Mockito.verify(bookDao).delete(books.get(1));
    }

    @Test
    void getBook() {
        Mockito.when(bookDao.findById(1)).thenReturn(Optional.of(BookData.getBookForAnnaKarenina()));
        BookDto actual = bookService.getBook(1);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getId(), actual.id);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getTitle(), actual.title);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getId(), actual.author.id);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getName(), actual.author.name);
    }

    @Test
    void getBookWithComments() {
        Book book = BookData.getBookForAnnaKarenina();
        book.getComments().add(BookCommentData.KARENINA_COMMENT_1);
        book.getComments().add(BookCommentData.KARENINA_COMMENT_2);
        Mockito.when(bookDao.findById(1)).thenReturn(Optional.of(book));
        BookWithCommentsDto actual = bookService.getBookWithComments(1);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getId(), actual.id);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getTitle(), actual.title);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getId(), actual.author.id);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getName(), actual.author.name);
        Assertions.assertEquals(BookCommentData.KARENINA_COMMENT_1.getId(), actual.comments.get(0).id);
        Assertions.assertEquals(BookCommentData.KARENINA_COMMENT_2.getId(), actual.comments.get(1).id);
    }
}
