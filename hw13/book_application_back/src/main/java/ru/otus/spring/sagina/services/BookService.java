package ru.otus.spring.sagina.services;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dto.request.BookDtoRequest;
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.repository.AuthorRepository;
import ru.otus.spring.sagina.repository.BookRepository;
import ru.otus.spring.sagina.repository.GenreRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final AclBookService aclBookService;

    public BookService(BookRepository bookRepository,
                       GenreRepository genreRepository,
                       AuthorRepository authorRepository,
                       AclBookService aclBookService) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.aclBookService = aclBookService;
    }

    @Transactional
    public Book createBook(BookDtoRequest book) {
        Author author = getAuthorById(book.getAuthor().getId());
        Book createdBook = new Book();
        createdBook.setName(book.getName());
        createdBook.setDescription(book.getDescription());
        createdBook.setAuthor(author);
        createdBook.setGenres(genreRepository.findAllByIdIn(book.getGenreIds()));
        Book saved = bookRepository.save(createdBook);
        aclBookService.createAcl(saved);
        return saved;
    }

    @Transactional
    public Book updateBook(BookDtoRequest book) {
        Book bookForUpdate = getBook(book.getId());
        bookForUpdate.setAuthor(getAuthorById(book.getAuthor().getId()));
        bookForUpdate.setName(book.getName());
        bookForUpdate.setDescription(book.getDescription());
        bookForUpdate.setGenres(genreRepository.findAllByIdIn(book.getGenreIds()));
        return bookRepository.save(bookForUpdate);
    }

    @Transactional
    public void deleteBook(Long bookId) {
        verifyBook(bookId);
        bookRepository.deleteById(bookId);
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("не найдена книга с id=%s", id)));
    }

    private Author getAuthorById(Long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(String.format("не найден автор с id=%s", authorId)));
    }

    private void verifyBook(Long id) {
        getBook(id);
    }
}
