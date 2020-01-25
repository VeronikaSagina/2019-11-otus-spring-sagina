package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.sagina.dao.AuthorDao;
import ru.otus.spring.sagina.dao.BookDao;
import ru.otus.spring.sagina.dao.GenreDao;
import ru.otus.spring.sagina.domain.Author;
import ru.otus.spring.sagina.domain.Book;
import ru.otus.spring.sagina.dto.mapper.BookDtoMapper;
import ru.otus.spring.sagina.dto.request.CreateBookDto;
import ru.otus.spring.sagina.dto.request.UpdateBookDto;
import ru.otus.spring.sagina.dto.response.BookDto;
import ru.otus.spring.sagina.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
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
        Author author = authorDao.getById(book.authorId);
        Book createdBook = new Book(book.title, author);
        createdBook.getGenres().addAll(genreDao.getByIds(book.genreIds));
        return BookDtoMapper.toDto(bookDao.create(createdBook));
    }

    @Transactional
    public BookDto updateBook(UpdateBookDto book) {
        if (!bookDao.existsById(book.id)) {
            throw new NotFoundException(String.format("не найдена книга с id=%s", book.id));
        }
        Author author = book.authorId == null ? null : authorDao.getById(book.authorId);
        Book bookForUpdate = new Book(book.id, book.title, author);
        bookForUpdate.getGenres().addAll(genreDao.getByIds(bookDao.getBookGenresIds(book.id)));
        bookDao.update(bookForUpdate);
        Book updatedBook = bookDao.getById(book.id);
        updatedBook.getGenres().addAll(genreDao.getByIds(bookDao.getBookGenresIds(book.id)));
        return BookDtoMapper.toDto(updatedBook);
    }

    @Transactional
    public void deleteBooks(List<Integer> bookIds) {
        bookDao.delete(bookIds);
    }

    public BookDto getBookByTitle(String title) {
        Book book = bookDao.getByTitle(title);
        book.getGenres().addAll(genreDao.getByIds(bookDao.getBookGenresIds(book.getId())));
        return BookDtoMapper.toDto(book);
    }

    public List<BookDto> getAllBooks() {
        List<Book> books = bookDao.getAll();
        books.forEach(b -> b.getGenres().addAll(genreDao.getByIds(bookDao.getBookGenresIds(b.getId()))));
        return books.stream()
                .map(BookDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> getAllBooksByAuthorName(String name) {
        List<Author> authors = authorDao.getByName(name);
        if (authors.isEmpty()) {
            throw new NotFoundException(String.format("не найден автор %s", name));
        }
        List<Book> books = new ArrayList<>();
        authors.forEach(a -> books.addAll(bookDao.getAllByAuthor(a)));
        books.forEach(b -> b.getGenres().addAll(genreDao.getByIds(bookDao.getBookGenresIds(b.getId()))));
        return books.stream()
                .map(BookDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> getBooksByGenreId(int genreId) {
        List<Book> books = bookDao.getBooksByGenreId(genreId);
        books.forEach(b -> b.getGenres().addAll(genreDao.getByIds(bookDao.getBookGenresIds(b.getId()))));
        return books.stream()
                .map(BookDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    public boolean existsByAuthorId(int authorId) {
        return bookDao.existsByAuthorId(authorId);
    }

    public BookDto getBook(int id) {
        Book book = bookDao.getById(id);
        book.getGenres().addAll(genreDao.getByIds(bookDao.getBookGenresIds(book.getId())));
        return BookDtoMapper.toDto(book);
    }

    List<Integer> getBooksIdByAuthorId(int authorId) {
        return bookDao.getBooksIdByAuthorId(authorId);
    }
}
