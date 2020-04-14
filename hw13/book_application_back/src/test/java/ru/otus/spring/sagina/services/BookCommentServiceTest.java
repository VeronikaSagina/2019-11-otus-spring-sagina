package ru.otus.spring.sagina.services;

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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        BookCommentDtoRequest bookCommentDto = new BookCommentDtoRequest("", 1L,
                BookCommentData.KARENINA_COMMENT_1.getMessage());
        Book book = new Book();
        book.setId(1L);
        BookComment expected = new BookComment(1L, bookCommentDto.getMessage(), book);
        Mockito.when(bookService.getBook(1L)).thenReturn(book);
        Mockito.when(bookCommentRepository.save(new BookComment(null, bookCommentDto.getMessage(), book)))
                .thenReturn(expected);
        BookComment actual =
                bookCommentService.create(new BookCommentDtoRequest(null, book.getId(), bookCommentDto.getMessage()));

        Mockito.verify(bookCommentRepository).save(new BookComment(null, bookCommentDto.getMessage(), book));
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getBook().getId(), actual.getBook().getId());
    }
}
