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
import ru.otus.spring.sagina.dto.response.GenreDto;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.testdata.AuthorData;
import ru.otus.spring.sagina.testdata.BookData;
import ru.otus.spring.sagina.testdata.GenreData;

import java.util.List;
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
        Mockito.when(bookDao.getByTitle("эльф")).thenReturn(BookData.getBookForBloodOfElves());
        Mockito.when(bookDao.getBookGenresIds(BookData.BLOOD_OF_ELVES.getId())).thenReturn(List.of(3, 4));
        Mockito.when(genreDao.getByIds(List.of(3, 4))).thenReturn(Set.of(GenreData.NOVEL, GenreData.FANTASY));
        BookDto actual = bookService.getBookByTitle("эльф");

        Mockito.verify(bookDao).getByTitle("эльф");
        Mockito.verify(bookDao).getBookGenresIds(BookData.BLOOD_OF_ELVES.getId());
        Mockito.verify(genreDao).getByIds(List.of(3, 4));

        Assertions.assertEquals(2, actual.genres.size());
        Assertions.assertTrue(actual.genres.containsAll(Set.of(GenreDtoMapper.toDto(GenreData.NOVEL),
                GenreDtoMapper.toDto(GenreData.FANTASY))));
    }

    @Test
    void getAllBooks() {
        Mockito.when(bookDao.getAll()).thenReturn(List.of(
                BookData.getBookForAnnaKarenina(), BookData.getBookForLordOfTheRing()));
        Mockito.when(bookDao.getBookGenresIds(BookData.ANNA_KARENINA.getId())).thenReturn(List.of(1));
        Mockito.when(bookDao.getBookGenresIds(BookData.LORD_OF_THE_RINGS.getId())).thenReturn(List.of(1, 3));
        Mockito.when(genreDao.getByIds(List.of(1))).thenReturn(Set.of(GenreData.NOVEL));
        Mockito.when(genreDao.getByIds(List.of(1, 3))).thenReturn(Set.of(GenreData.NOVEL, GenreData.FANTASY));

        List<BookDto> actual = bookService.getAllBooks();

        GenreDto genreDto1 = GenreDtoMapper.toDto(GenreData.NOVEL);
        GenreDto genreDto2 = GenreDtoMapper.toDto(GenreData.FANTASY);

        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(Set.of(genreDto1), actual.get(0).genres);
        Assertions.assertEquals(Set.of(genreDto1, genreDto2), actual.get(1).genres);
    }

    @Test
    void getAllBooksByAuthorName() {
        Mockito.when(authorDao.getByName("п")).thenReturn(List.of(AuthorData.SAPKOWSKI, AuthorData.PELEVIN));

        Mockito.when(bookDao.getAllByAuthor(AuthorData.SAPKOWSKI))
                .thenReturn(List.of(BookData.getBookForBloodOfElves()));
        Mockito.when(bookDao.getAllByAuthor(AuthorData.PELEVIN))
                .thenReturn(List.of(BookData.getBookForSnuff()));

        Mockito.when(bookDao.getBookGenresIds(BookData.BLOOD_OF_ELVES.getId()))
                .thenReturn(List.of(GenreData.NOVEL.getId(), GenreData.FANTASY.getId()));
        Mockito.when(bookDao.getBookGenresIds(BookData.SNUFF.getId()))
                .thenReturn(List.of(GenreData.NOVEL.getId(), GenreData.FANTASTIC.getId()));

        Mockito.when(genreDao.getByIds(List.of(GenreData.NOVEL.getId(), GenreData.FANTASY.getId())))
                .thenReturn(Set.of(GenreData.NOVEL, GenreData.FANTASY));
        Mockito.when(genreDao.getByIds(List.of(GenreData.NOVEL.getId(), GenreData.FANTASTIC.getId())))
                .thenReturn(Set.of(GenreData.NOVEL, GenreData.FANTASTIC));

        List<BookDto> actual = bookService.getAllBooksByAuthorName("п");
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(AuthorData.SAPKOWSKI.getId(), actual.get(0).author.id);
        Assertions.assertEquals(AuthorData.SAPKOWSKI.getName(), actual.get(0).author.name);
        Assertions.assertEquals(AuthorData.PELEVIN.getId(), actual.get(1).author.id);
        Assertions.assertEquals(AuthorData.PELEVIN.getName(), actual.get(1).author.name);

        Assertions.assertEquals(Set.of(
                GenreDtoMapper.toDto(GenreData.NOVEL), GenreDtoMapper.toDto(GenreData.FANTASY)), actual.get(0).genres);
        Assertions.assertEquals(Set.of(
                GenreDtoMapper.toDto(GenreData.NOVEL), GenreDtoMapper.toDto(GenreData.FANTASTIC)), actual.get(1).genres);
    }

    @Test
    void getBooksByGenreId() {
        Mockito.when(bookDao.getBooksByGenreId(1))
                .thenReturn(List.of(BookData.getBookForAnnaKarenina(), BookData.getBookForLordOfTheRing()));

        Mockito.when(bookDao.getBookGenresIds(BookData.ANNA_KARENINA.getId()))
                .thenReturn(List.of(GenreData.NOVEL.getId()));
        Mockito.when(bookDao.getBookGenresIds(BookData.LORD_OF_THE_RINGS.getId()))
                .thenReturn(List.of(GenreData.NOVEL.getId(), GenreData.FANTASY.getId()));

        Mockito.when(genreDao.getByIds(List.of(GenreData.NOVEL.getId()))).thenReturn(Set.of(GenreData.NOVEL));
        Mockito.when(genreDao.getByIds(List.of(GenreData.NOVEL.getId(), GenreData.FANTASY.getId())))
                .thenReturn(Set.of(GenreData.NOVEL, GenreData.FANTASY));

        List<BookDto> actual = bookService.getBooksByGenreId(1);
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(BookData.ANNA_KARENINA.getId(), actual.get(0).id);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getTitle(), actual.get(0).title);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getId(), actual.get(0).author.id);
        Assertions.assertEquals(1, actual.get(0).genres.size());

        Assertions.assertEquals(BookData.LORD_OF_THE_RINGS.getId(), actual.get(1).id);
        Assertions.assertEquals(BookData.LORD_OF_THE_RINGS.getTitle(), actual.get(1).title);
        Assertions.assertEquals(BookData.LORD_OF_THE_RINGS.getAuthor().getId(), actual.get(1).author.id);
        Assertions.assertEquals(2, actual.get(1).genres.size());
    }

    @Test
    void createBook() {
        CreateBookDto bookDto = new CreateBookDto(BookData.SNUFF.getTitle(), AuthorData.PELEVIN.getId(),
                List.of(GenreData.FANTASTIC.getId(), GenreData.NOVEL.getId()));
        Mockito.when(authorDao.getById(AuthorData.PELEVIN.getId())).thenReturn(AuthorData.PELEVIN);
        Mockito.when(bookDao.getIdFromSequence()).thenReturn(5);
        Mockito.when(genreDao.getByIds(bookDto.genreIds)).thenReturn(Set.of(GenreData.FANTASTIC, GenreData.NOVEL));

        BookDto actual = bookService.createBook(bookDto);
        Mockito.verify(bookDao).create(Mockito.any());

        Assertions.assertEquals(BookData.SNUFF.getId(), actual.id);
        Assertions.assertEquals(BookData.SNUFF.getAuthor().getId(), actual.author.id);
        Assertions.assertEquals(BookData.SNUFF.getAuthor().getName(), actual.author.name);
        Assertions.assertEquals(BookData.SNUFF.getTitle(), actual.title);
        Assertions.assertEquals(Set.of(GenreDtoMapper.toDto(GenreData.FANTASTIC), GenreDtoMapper.toDto(GenreData.NOVEL))
                , actual.genres);
    }

    @Test
    void updateBook() {
        UpdateBookDto updateBookDto = new UpdateBookDto(1, null, null, List.of(1, 2));
        Mockito.when(bookDao.existsById(updateBookDto.id)).thenReturn(true);

        Mockito.when(genreDao.getByIds(updateBookDto.genreIds))
                .thenReturn(Set.of(GenreData.NOVEL, GenreData.DETECTIVE));
        Mockito.when(bookDao.getById(updateBookDto.id)).thenReturn(
                new Book(updateBookDto.id, BookData.ANNA_KARENINA.getTitle(), BookData.ANNA_KARENINA.getAuthor()));
        Mockito.when(bookDao.getBookGenresIds(updateBookDto.id)).thenReturn(List.of(1, 2));
        Mockito.when(genreDao.getByIds(List.of(1, 2))).thenReturn(Set.of(GenreData.NOVEL, GenreData.DETECTIVE));
        BookDto actual = bookService.updateBook(updateBookDto);
        Mockito.verify(bookDao).update(Mockito.any());

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
        Assertions.assertThrows(NotFoundException.class,
                () -> bookService.updateBook(new UpdateBookDto(1, null, null, List.of(1, 2))));
    }

    @Test
    void deleteBooks() {
        bookService.deleteBooks(List.of(1, 2));
        Mockito.verify(bookDao).delete(List.of(1, 2));
    }

    @Test
    void existsByAuthorId() {
        Mockito.when(bookDao.existsByAuthorId(1)).thenReturn(true);
        Assertions.assertTrue(bookService.existsByAuthorId(1));
    }

    @Test
    void getBook() {
        Mockito.when(bookDao.getById(1)).thenReturn(BookData.getBookForAnnaKarenina());
        Mockito.when(bookDao.getBookGenresIds(1)).thenReturn(List.of(1, 2));
        Mockito.when(genreDao.getByIds(List.of(1, 2))).thenReturn(Set.of(GenreData.NOVEL, GenreData.DETECTIVE));
        BookDto actual = bookService.getBook(1);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getId(), actual.id);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getTitle(), actual.title);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getId(), actual.author.id);
        Assertions.assertEquals(BookData.ANNA_KARENINA.getAuthor().getName(), actual.author.name);
        Assertions.assertEquals(2, actual.genres.size());
        Assertions.assertTrue(actual.genres.containsAll(
                Set.of(GenreDtoMapper.toDto(GenreData.NOVEL), GenreDtoMapper.toDto(GenreData.DETECTIVE))));
    }

    @Test
    void getBooksIdByAuthorId() {
        Mockito.when(bookDao.getBooksIdByAuthorId(1)).thenReturn(List.of(1, 2, 3));
        Assertions.assertEquals(List.of(1, 2, 3), bookService.getBooksIdByAuthorId(1));
    }
}
