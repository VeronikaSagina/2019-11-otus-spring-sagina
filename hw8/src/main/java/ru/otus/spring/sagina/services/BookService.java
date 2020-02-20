package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dto.request.CreateBookDto;
import ru.otus.spring.sagina.dto.request.UpdateBookDto;
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.repository.AuthorRepository;
import ru.otus.spring.sagina.repository.BookCommentRepository;
import ru.otus.spring.sagina.repository.BookRepository;
import ru.otus.spring.sagina.repository.GenreRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final BookCommentRepository bookCommentRepository;

    public BookService(BookRepository bookRepository,
                       GenreRepository genreRepository,
                       AuthorRepository authorRepository,
                       BookCommentRepository bookCommentRepository) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.bookCommentRepository = bookCommentRepository;
    }

    public Book createBook(CreateBookDto book) {
        Author author = getAuthorById(book.authorId);
        Book createdBook = new Book();
        createdBook.setTitle(book.title);
        createdBook.setAuthor(author);
        createdBook.setGenres(genreRepository.findAllByIdIn(book.genreIds));
        return bookRepository.save(createdBook);
    }

    public Book updateBook(UpdateBookDto book) {
        Book bookForUpdate = getBook(book.id);
        Optional.ofNullable(book.authorId)
                .ifPresent(authorId -> bookForUpdate.setAuthor(getAuthorById(authorId)));
        Optional.ofNullable(book.title)
                .ifPresent(bookForUpdate::setTitle);
        Optional.ofNullable(book.genreIds)
                .ifPresent(it -> bookForUpdate.setGenres(genreRepository.findAllByIdIn(it)));
        return bookRepository.save(bookForUpdate);
    }

    public void deleteBooksByAuthorId(String authorId) {
        verifyAuthor(authorId);
        bookRepository.deleteAll(bookRepository.findAllByAuthorIdIn(List.of(authorId)));
    }

    public void deleteBook(String bookId) {
        verifyBook(bookId);
        bookRepository.deleteById(bookId);
    }

    public List<Book> getBookByTitle(String title) {
        return bookRepository.findAllByTitleContainingIgnoreCaseOrderByTitle(title);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getAllBooksByAuthorName(String name) {
        List<String> authorIds = authorRepository.findAllByNameContainingIgnoreCaseOrderByName(name).stream()
                .map(Author::getId)
                .collect(Collectors.toList());
        if (authorIds.isEmpty()) {
            throw new NotFoundException(String.format("не найден автор %s", name));
        }
        return bookRepository.findAllByAuthorIdIn(authorIds).stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());
    }

    public List<Book> getBooksByGenreId(String genreId) {
        return bookRepository.findAllByGenreId(genreId);
    }

    public Book getBookWithComments(String id) {
        Book book = getBook(id);
        book.setComments(bookCommentRepository.findAllByBookId(id));
        return book;
    }

    public Book getBook(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("не найдена книга с id=%s", id)));
    }

    private Author getAuthorById(String authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(String.format("не найден автор с id=%s", authorId)));
    }

    private void verifyBook(String id) {
        getBook(id);
    }

    private void verifyAuthor(String id) {
        getAuthorById(id);
    }
}
