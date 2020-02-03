package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.sagina.dao.BookCommentDao;
import ru.otus.spring.sagina.dao.BookDao;
import ru.otus.spring.sagina.domain.Book;
import ru.otus.spring.sagina.domain.BookComment;
import ru.otus.spring.sagina.dto.mapper.BookCommentDtoMapper;
import ru.otus.spring.sagina.dto.request.CreateBookCommentDto;
import ru.otus.spring.sagina.dto.response.BookCommentDto;
import ru.otus.spring.sagina.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookCommentService {

    private final BookCommentDao bookCommentDao;
    private final BookDao bookDao;

    public BookCommentService(BookCommentDao bookCommentDao, BookDao bookDao) {
        this.bookCommentDao = bookCommentDao;
        this.bookDao = bookDao;
    }

    public BookCommentDto findById(int id) {
        BookComment bookComment = bookCommentDao.findById(id).orElseThrow(() -> new NotFoundException(
                String.format("не найден комментарий с id=%s", id)));
        return BookCommentDtoMapper.toDto(bookComment);
    }

    public List<BookCommentDto> getAllByBookId(int bookId) {
        bookDao.findById(bookId).orElseThrow(
                () -> new NotFoundException(String.format("не найдена книга с id=%s", bookId)));
        List<BookComment> comments = bookCommentDao.getAllByBookId(bookId);
        return comments.stream()
                .map(BookCommentDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookCommentDto create(CreateBookCommentDto comment) {
        Book book = bookDao.findById(comment.bookId).orElseThrow(
                () -> new NotFoundException(String.format("не найдена книга с id=%s", comment.bookId)));
        BookComment bookComment = new BookComment(comment.message, book);
        BookComment saved = bookCommentDao.save(bookComment);
        return BookCommentDtoMapper.toDto(saved);
    }

    @Transactional
    public void delete(BookComment bookComment) {
        bookCommentDao.delete(bookComment);
    }

    @Transactional
    public void deleteById(int id) {
        bookCommentDao.deleteById(id);
    }

    @Transactional
    public void deleteAllByBookId(int bookId) {
        bookDao.findById(bookId).orElseThrow(
                () -> new NotFoundException(String.format("не найдена книга с id=%s", bookId)));
        bookCommentDao.deleteAllByBookId(bookId);
    }
}
