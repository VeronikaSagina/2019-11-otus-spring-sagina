package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dto.request.BookCommentDtoRequest;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.entity.BookComment;
import ru.otus.spring.sagina.repository.BookCommentRepository;

import java.util.List;

@Service
public class BookCommentService {
    private final BookService bookService;
    private final BookCommentRepository bookCommentRepository;

    public BookCommentService(BookService bookService,
                              BookCommentRepository bookCommentRepository) {
        this.bookService = bookService;
        this.bookCommentRepository = bookCommentRepository;
    }

    public BookComment create(BookCommentDtoRequest comment) {
        Book book = bookService.getBook(comment.bookId);
        BookComment bookComment = new BookComment();
        bookComment.setMessage(comment.message);
        bookComment.setBook(book);
        bookComment.setBook(book);
        return bookCommentRepository.save(bookComment);
    }

    public List<BookComment> getAllByBookId(String bookId) {
        return bookCommentRepository.findAllByBookId(bookId);
    }
}
