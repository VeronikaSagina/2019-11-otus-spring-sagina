package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.entity.BookComment;
import ru.otus.spring.sagina.repository.BookCommentRepository;

@Service
public class BookCommentService {
    private final BookService bookService;
    private final BookCommentRepository bookCommentRepository;

    public BookCommentService(BookService bookService,
                              BookCommentRepository bookCommentRepository) {
        this.bookService = bookService;
        this.bookCommentRepository = bookCommentRepository;
    }

    public BookComment create(String bookId, String message) {
        Book book = bookService.getBook(bookId);
        BookComment bookComment = new BookComment();
        bookComment.setMessage(message);
        bookComment.setBook(book);
        return bookCommentRepository.save(bookComment);
    }
}
