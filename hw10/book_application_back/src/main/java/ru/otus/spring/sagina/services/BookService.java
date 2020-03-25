package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dto.request.BookDtoRequest;
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.repository.AuthorRepository;
import ru.otus.spring.sagina.repository.BookCommentRepository;
import ru.otus.spring.sagina.repository.BookRepository;
import ru.otus.spring.sagina.repository.GenreRepository;

import java.util.List;

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

    public Book createBook(BookDtoRequest book) {
        Author author = getAuthorById(book.author.id);
        Book createdBook = new Book();
        createdBook.setName(book.name);
        createdBook.setDescription(book.description);
        createdBook.setAuthor(author);
        createdBook.setGenres(genreRepository.findAllByIdIn(book.genreIds));
        return bookRepository.save(createdBook);
    }

    public Book updateBook(BookDtoRequest book) {
        Book bookForUpdate = getBook(book.id);
        bookForUpdate.setAuthor(getAuthorById(book.author.id));
        bookForUpdate.setName(book.name);
        bookForUpdate.setDescription(book.description);
        bookForUpdate.setGenres(genreRepository.findAllByIdIn(book.genreIds));
        return bookRepository.save(bookForUpdate);
    }

    public void deleteBook(String bookId) {
        verifyBook(bookId);
        bookCommentRepository.deleteAllByBookId(bookId);
        bookRepository.deleteById(bookId);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
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
}
