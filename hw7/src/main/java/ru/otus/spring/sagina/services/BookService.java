package ru.otus.spring.sagina.services;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public Book createBook(CreateBookDto book) {
        Author author = getAuthorById(book.authorId);
        Book createdBook = new Book();
        createdBook.setTitle(book.title);
        createdBook.setAuthor(author);
        createdBook.setGenres(genreRepository.findAllByIdIn(book.genreIds));
        return bookRepository.save(createdBook);
    }

    @Transactional
    public Book updateBook(UpdateBookDto book) {
        Book bookForUpdate = bookRepository.findById(book.id)
                .orElseThrow(() -> new NotFoundException(String.format("не найдена книга с id=%s", book.id)));
        Optional.ofNullable(book.authorId)
                .ifPresent(authorId -> bookForUpdate.setAuthor(getAuthorById(authorId)));
        Optional.ofNullable(book.title)
                .ifPresent(bookForUpdate::setTitle);
        Optional.ofNullable(book.genreIds)
                .ifPresent(it -> bookForUpdate.setGenres(genreRepository.findAllByIdIn(it)));
        return bookRepository.save(bookForUpdate);
    }

    @Transactional
    public void deleteBooksByAuthorId(int authorId) {
        Author author = getAuthorById(authorId);
        bookCommentRepository.deleteAllByBookIdIn(
                author.getBooks().stream()
                        .map(Book::getId)
                        .collect(Collectors.toList()));
        bookRepository.deleteAll(author.getBooks());
    }

    @Transactional
    public void deleteBook(int bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new NotFoundException(String.format("не найдена книга с id=%s", bookId));
        }
        bookRepository.deleteById(bookId);
    }

    public List<Book> getBookByTitle(String title) {
        return bookRepository.findAllByTitleContainingIgnoreCaseOrderByTitle(title);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getAllBooksByAuthorName(String name) {
        List<Integer> authorIds = authorRepository.findAllByNameContainingIgnoreCaseOrderByName(name).stream()
                .map(Author::getId)
                .collect(Collectors.toList());
        if (authorIds.isEmpty()) {
            throw new NotFoundException(String.format("не найден автор %s", name));
        }
        return bookRepository.findAllByAuthorIdIn(authorIds).stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());
    }

    public List<Book> getBooksByGenreId(int genreId) {
        return bookRepository.findAllByGenreId(genreId);
    }

    public Book getBook(int id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("не найдена книга с id=%s", id)));
    }

    @Transactional
    public Book getBookWithComments(int id) {
        Hibernate.initialize(getBook(id).getComments());
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("не найдена книга с id=%s", id)));
    }

    boolean existsById(int bookId) {
        return bookRepository.existsById(bookId);
    }

    private Author getAuthorById(int authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(String.format("не найден автор с id=%s", authorId)));
    }
}
