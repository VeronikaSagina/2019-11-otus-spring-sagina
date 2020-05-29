package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.sagina.dto.request.CreateBookCommentDto;
import ru.otus.spring.sagina.dto.request.UpdateBookCommentDto;
import ru.otus.spring.sagina.entity.BookComment;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.repository.BookCommentRepository;
import ru.otus.spring.sagina.repository.BookRepository;

import java.util.List;
import java.util.UUID;

@Service
public class BookCommentService {
    private final BookRepository bookRepository;
    private final BookCommentRepository bookCommentRepository;
    private final UserSessionService userSessionService;

    public BookCommentService(BookRepository bookRepository,
                              BookCommentRepository bookCommentRepository,
                              UserSessionService userSessionService) {
        this.bookRepository = bookRepository;
        this.bookCommentRepository = bookCommentRepository;
        this.userSessionService = userSessionService;
    }

    public List<BookComment> getAllByBookId(UUID bookId) {
        return bookCommentRepository.findAllByBookId(bookId);
    }

    @Transactional
    public BookComment create(CreateBookCommentDto createBookCommentDto) {
        BookComment bookComment = new BookComment();
        bookComment.setMessage(createBookCommentDto.getMessage());
        bookComment.setBook(bookRepository.getOne(createBookCommentDto.getBookId()));
        bookComment.setUser(userSessionService.getCurrentUserProxy());
        return bookCommentRepository.save(bookComment);
    }

    @Transactional
    public BookComment update(UpdateBookCommentDto bookCommentDto) {
        return bookCommentRepository.findById(bookCommentDto.getId())
                .map(it -> {
                    it.setMessage(bookCommentDto.getMessage());
                    return it;
                })
                .orElseThrow(() -> new NotFoundException("Не найден комментарий с id: " + bookCommentDto.getId()));
    }

    @Transactional
    public void delete(UUID id) {
        bookCommentRepository.findById(id)
                .ifPresentOrElse(bookCommentRepository::delete, () -> {
                    throw new NotFoundException("Не найден комментарий с id: " + id);
                });
    }
}
