package ru.otus.spring.sagina.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.spring.sagina.dto.request.CreateBookDto;
import ru.otus.spring.sagina.dto.request.UpdateBookDto;
import ru.otus.spring.sagina.dto.response.FileResultDto;
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.repository.AuthorRepository;
import ru.otus.spring.sagina.repository.BookRepository;
import ru.otus.spring.sagina.repository.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {
    private final static Logger LOGGER = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final MinioService minioService;
    private final EmailSenderService emailSenderService;

    public BookService(BookRepository bookRepository,
                       GenreRepository genreRepository,
                       AuthorRepository authorRepository,
                       MinioService minioService,
                       EmailSenderService emailSenderService) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.minioService = minioService;
        this.emailSenderService = emailSenderService;
    }

    @Transactional(rollbackFor = Throwable.class)
    public Book createBook(CreateBookDto book, MultipartFile multipartFile) {
        Author author = getAuthorById(book.getAuthor().getId());
        Book createdBook = new Book();
        createdBook.setName(book.getName());
        createdBook.setDescription(book.getDescription());
        createdBook.setAuthor(author);
        createdBook.setGenres(genreRepository.findAllByIdIn(book.getGenreIds()));
        Book savedBook = bookRepository.save(createdBook);
        minioService.uploadBook(savedBook, multipartFile);
        return savedBook;
    }

    @Transactional
    public Book updateBook(UpdateBookDto book) {
        Book bookForUpdate = getBook(book.getId());
        Optional.ofNullable(book.getAuthor())
                .ifPresent(it -> bookForUpdate.setAuthor(getAuthorById(it.getId())));
        Optional.ofNullable(book.getName())
                .ifPresent(bookForUpdate::setName);
        Optional.ofNullable(book.getDescription())
                .ifPresent(bookForUpdate::setDescription);
        Optional.ofNullable(book.getGenreIds())
                .filter(it -> it.size() > 0)
                .ifPresent(it -> bookForUpdate.setGenres(genreRepository.findAllByIdIn(it)));
        return bookRepository.save(bookForUpdate);
    }

    @Transactional
    public void deleteBook(UUID bookId) {
        verifyBook(bookId);
        minioService.delete(bookId);
        bookRepository.deleteById(bookId);
    }

    public List<Book> getAllBooks(UUID authorId, String query) {
        if (query.isBlank() && authorId == null) {
            LOGGER.debug("getting all books");
            return bookRepository.findAll(Sort.by("name"));
        }
        LOGGER.debug("getting books by author id [{}] and name containing [{}]", authorId, query);
        return bookRepository.findAllByNameAndAuthorId(authorId, query, Sort.by("name"));
    }

    public Book getBook(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("не найдена книга с id=%s", id)));
    }

    private Author getAuthorById(UUID authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(String.format("не найден автор с id=%s", authorId)));
    }

    private void verifyBook(UUID id) {
        getBook(id);
    }

    public FileResultDto getFile(UUID id) {
        return minioService.getFile(id);
    }

    public void sendByEmail(UUID bookId) {
        emailSenderService.sendEmail(bookId);
    }
}
