package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public BookComment getById(int id) {
        return bookCommentRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("не найден комментарий с id=%s", id)));
    }

    @Transactional
    public List<BookComment> getAllByBookId(int bookId) {
        return bookService.getBookWithComments(bookId).getComments();
    }

    @Transactional
    public BookComment create(CreateBookCommentDto comment) {
        if (!bookService.existsById(comment.bookId)) {
            throw new NotFoundException(String.format("не найдена книга с id=%s", comment.bookId));
        }
        BookComment bookComment = new BookComment();
        bookComment.setMessage(comment.message);
        Book book = new Book();
        book.setId(comment.bookId);
        bookComment.setBook(book);
        return bookCommentRepository.save(bookComment);
    }

    @Transactional
    public void deleteById(int id) {
        if (!bookCommentRepository.existsById(id)) {
            throw new NotFoundException(String.format("не найден комментарий с id=%s", id));
        }
        bookCommentRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllByBookId(int bookId) {
        if (!bookService.existsById(bookId)) {
            throw new NotFoundException(String.format("не найдена книга с id=%s", bookId));
        }
        bookCommentRepository.deleteAllByBookIdIn(List.of(bookId));
    }
}
