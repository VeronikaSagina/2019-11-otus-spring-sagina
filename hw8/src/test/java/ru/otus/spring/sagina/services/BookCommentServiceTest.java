package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.sagina.dto.request.CreateBookCommentDto;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.entity.BookComment;
import ru.otus.spring.sagina.repository.BookCommentRepository;
import ru.otus.spring.sagina.testdata.BookCommentData;
import ru.otus.spring.sagina.testdata.BookData;
import ru.otus.spring.sagina.utils.TestUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class BookCommentServiceTest {
    @Mock
    private BookService bookService;
    @Mock
    private BookCommentRepository bookCommentRepository;
    @InjectMocks
    private BookCommentService bookCommentService;

    @Test
    public void createTest() {
        CreateBookCommentDto bookCommentDto = new CreateBookCommentDto("1",
                BookCommentData.KARENINA_COMMENT_1.getMessage());
        Book book = new Book();
        book.setId("1");
        BookComment expected = new BookComment("1", bookCommentDto.message, book);
        Mockito.when(bookService.getBook("1")).thenReturn(book);
        Mockito.when(bookCommentRepository.save(Mockito.any())).thenReturn(expected);
        BookComment actual = bookCommentService.create(bookCommentDto);
        Mockito.verify(bookCommentRepository).save(new BookComment(null, bookCommentDto.message, book));
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getMessage(), actual.getMessage());
        Assertions.assertEquals(expected.getBook().getId(), actual.getBook().getId());
    }

    @Test
    public void deleteByIdTest() {
        Mockito.when(bookCommentRepository.findById("1")).thenReturn(Optional.of(BookCommentData.KARENINA_COMMENT_1));
        bookCommentService.deleteById("1");
        Mockito.verify(bookCommentRepository).delete(BookCommentData.KARENINA_COMMENT_1);
    }

    @Test
    public void deleteAllByBookIdTest() {
        bookCommentService.deleteAllByBookId("1");
        Mockito.verify(bookCommentRepository).deleteAllByBookId("1");
    }

    @Test
    public void getByIdTest() {
        Mockito.when(bookCommentRepository.findById("1")).thenReturn(Optional.of(BookCommentData.KARENINA_COMMENT_1));
        BookComment actual = bookCommentService.getById("1");
        Assertions.assertEquals(BookCommentData.KARENINA_COMMENT_1.getId(), actual.getId());
        Assertions.assertEquals(BookCommentData.KARENINA_COMMENT_1.getMessage(), actual.getMessage());
        Assertions.assertEquals(BookCommentData.KARENINA_COMMENT_1.getBook().getId(), actual.getBook().getId());
    }

    @Test
    public void getAllByBookIdTest() {
        Book book = BookData.getBookForAnnaKarenina();
        book.getComments().add(BookCommentData.KARENINA_COMMENT_1);
        book.getComments().add(BookCommentData.KARENINA_COMMENT_2);
        Mockito.when(bookService.getBookWithComments("1")).thenReturn(book);
        List<BookComment> actual = bookCommentService.getAllByBookId("1");
        TestUtils.compare(book.getComments(), actual,
                Comparator.comparing(BookComment::getId)
                        .thenComparing(BookComment::getMessage)
                        .thenComparing(it -> it.getBook().getId()));
    }
}
