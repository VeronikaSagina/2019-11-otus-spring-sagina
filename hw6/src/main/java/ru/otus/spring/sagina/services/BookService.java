package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.sagina.dao.AuthorDao;
import ru.otus.spring.sagina.dao.BookDao;
import ru.otus.spring.sagina.dao.GenreDao;
import ru.otus.spring.sagina.domain.Author;
import ru.otus.spring.sagina.domain.Book;
import ru.otus.spring.sagina.domain.Genre;
import ru.otus.spring.sagina.dto.mapper.BookDtoMapper;
import ru.otus.spring.sagina.dto.mapper.BookWithCommentsDtoMapper;
import ru.otus.spring.sagina.dto.request.CreateBookDto;
import ru.otus.spring.sagina.dto.request.UpdateBookDto;
import ru.otus.spring.sagina.dto.response.BookDto;
import ru.otus.spring.sagina.dto.response.BookWithCommentsDto;
import ru.otus.spring.sagina.exceptions.NotFoundException;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    public BookService(BookDao bookDao, AuthorDao authorDao, GenreDao genreDao) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
    }

    @Transactional
    public BookDto createBook(CreateBookDto book) {
        Author author = authorDao.findById(book.authorId)
                .orElseThrow(() -> new NotFoundException(String.format("не найден автор с id=%s", book.authorId)));
        Book createdBook = new Book(book.title, author);
        createdBook.getGenres().addAll(genreDao.getByIds(book.genreIds));
        return BookDtoMapper.toDto(bookDao.save(createdBook));
    }

    @Transactional
    public BookDto updateBook(UpdateBookDto book) {
        Book bookForUpdate = bookDao.findById(book.id)
                .orElseThrow(() -> new NotFoundException(String.format("не найдена книга с id=%s", book.id)));
        Author author = book.authorId == null ? bookForUpdate.getAuthor() : authorDao.findById(book.authorId)
                .orElseThrow(() -> new NotFoundException(String.format("не найден автор с id=%s", book.authorId)));
        List<Genre> genres = book.genreIds != null
                ? genreDao.getByIds(book.genreIds)
                : bookForUpdate.getGenres();
        String title = (book.title == null || book.title.isBlank())
                ? bookForUpdate.getTitle()
                : book.title;
        Book book1 = new Book(book.id, title, author, genres);
        Book save = bookDao.save(book1);
        return BookDtoMapper.toDto(save);
    }

    @Transactional
    public void deleteBooks(List<Integer> bookIds) {
        List<Book> books = bookDao.getByIds(bookIds);
        books.forEach(bookDao::delete);
    }

    public List<BookDto> getBookByTitle(String title) {
        List<Book> books = bookDao.getByTitle(title);
        return books.stream()
                .map(BookDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> getAllBooks() {
        List<Book> books = bookDao.getAll();
        return books.stream()
                .map(BookDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> getAllBooksByAuthorName(String name) {
        List<Author> authors = authorDao.getByName(name);
        if (authors.isEmpty()) {
            throw new NotFoundException(String.format("не найден автор %s", name));
        }
        Set<Book> books = new HashSet<>();
        authors.forEach(a -> books.addAll(bookDao.getByAuthorId(a.getId())));
        return books.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .map(BookDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> getBooksByGenreId(int genreId) {
        List<Book> books = bookDao.getByGenreId(genreId);
        return books.stream()
                .map(BookDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    public BookDto getBook(int id) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("не найдена книга с id=%s", id)));
        return BookDtoMapper.toDto(book);
    }

    @Transactional
    public BookWithCommentsDto getBookWithComments(int id) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("не найдена книга с id=%s", id)));
        return BookWithCommentsDtoMapper.toDto(book);
    }
}
