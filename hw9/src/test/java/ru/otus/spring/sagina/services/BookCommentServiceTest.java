package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.sagina.dto.request.BookCommentDtoRequest;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.entity.BookComment;
import ru.otus.spring.sagina.repository.BookCommentRepository;
import ru.otus.spring.sagina.testdata.BookCommentData;

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
        BookCommentDtoRequest bookCommentDto = new BookCommentDtoRequest("", "1",
                BookCommentData.KARENINA_COMMENT_1.getMessage());
        Book book = new Book();
        book.setId("1");
        BookComment expected = new BookComment("1", bookCommentDto.message, book);
        Mockito.when(bookService.getBook("1")).thenReturn(book);
        Mockito.when(bookCommentRepository.save(Mockito.any())).thenReturn(expected);
        BookComment actual = bookCommentService.create(book.getId(), bookCommentDto.message);
        Mockito.verify(bookCommentRepository).save(new BookComment(null, bookCommentDto.message, book));
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getMessage(), actual.getMessage());
        Assertions.assertEquals(expected.getBook().getId(), actual.getBook().getId());
    }
}
