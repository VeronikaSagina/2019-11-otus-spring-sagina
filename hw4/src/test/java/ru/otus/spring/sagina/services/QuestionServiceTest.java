package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.sagina.dao.CsvReaderDao;
import ru.otus.spring.sagina.domain.Answer;
import ru.otus.spring.sagina.domain.Question;
import ru.otus.spring.sagina.domain.TestItem;

import java.util.List;

@SpringBootTest
public class QuestionServiceTest {
    private static final TestItem TEST_ITEM = new TestItem(
            new Question("Q1", List.of("A1", "A2")), new Answer(1));
    private static final TestItem TEST_ITEM_1 = new TestItem(
            new Question("Q2", List.of("A1", "A2", "A3")), new Answer(2));

    @Mock
    private CsvReaderDao csvReaderDao;
    @InjectMocks
    private QuestionServiceImpl questionService;

    @Test
    public void testGetQuestion() {
        Mockito.when(csvReaderDao.getTestItem(0)).thenReturn(TEST_ITEM);
        Question expected = TEST_ITEM.getQuestion();
        Question actual = questionService.getQuestion(0);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetQuestionException() {
        Mockito.when(csvReaderDao.getTestItem(Mockito.anyInt())).thenThrow(IndexOutOfBoundsException.class);
        Assertions.assertThrows(
                IndexOutOfBoundsException.class, () -> questionService.getQuestion(5));
    }

    @Test
    public void testGetAnswer() {
        Answer expected = TEST_ITEM_1.getAnswer();
        Mockito.when(csvReaderDao.getTestItem(1)).thenReturn(TEST_ITEM_1);
        Answer actual = questionService.getAnswer(1);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetAnswerException() {
        Mockito.when(csvReaderDao.getTestItem(Mockito.anyInt())).thenThrow(IndexOutOfBoundsException.class);
        Assertions.assertThrows(
                IndexOutOfBoundsException.class, () -> questionService.getAnswer(4));
    }

    @Test
    public void testGetTestSize() {
        Mockito.when(csvReaderDao.getSize()).thenReturn(2);
        Assertions.assertEquals(2, questionService.getTestSize());
    }
}
