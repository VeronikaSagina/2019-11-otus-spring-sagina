package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dto.request.CreateBookCommentDto;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.entity.BookComment;
import ru.otus.spring.sagina.exceptions.NotFoundException;
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

    public BookComment create(CreateBookCommentDto comment) {
        Book book = bookService.getBook(comment.bookId);
        BookComment bookComment = new BookComment();
        bookComment.setMessage(comment.message);
        bookComment.setBook(book);
        return bookCommentRepository.save(bookComment);
    }

    public void deleteById(String id) {
        BookComment comment = getById(id);
        bookCommentRepository.delete(comment);
    }

    public void deleteAllByBookId(String bookId) {
        bookCommentRepository.deleteAllByBookId(bookId);
    }

    public BookComment getById(String id) {
        return bookCommentRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("не найден комментарий с id=%s", id)));
    }

    public List<BookComment> getAllByBookId(String bookId) {
        return bookCommentRepository.findAllByBookId(bookId);
    }
}
