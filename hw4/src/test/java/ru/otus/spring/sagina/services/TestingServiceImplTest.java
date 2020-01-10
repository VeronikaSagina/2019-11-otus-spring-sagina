package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.sagina.domain.Question;
import ru.otus.spring.sagina.domain.Student;
import ru.otus.spring.sagina.domain.StudentResult;
import ru.otus.spring.sagina.exceptions.TestFinishedException;

import java.util.List;

@SpringBootTest
class TestingServiceImplTest {
    @Mock
    private QuestionServiceImpl questionService;
    @Mock
    private ResultServiceImpl resultService;
    @InjectMocks
    private TestingServiceImpl testingService;

    @Test
    void getNextQuestion() {
        Student student = new Student("Bob");
        StudentResult studentResult = new StudentResult(student.getName());
        Question question = new Question("Q1", List.of("A1", "A2"));
        Mockito.when(resultService.getStudentResult(student)).thenReturn(studentResult);
        Mockito.when(questionService.getTestSize()).thenReturn(5);
        Mockito.when(resultService.getCurrentQuestionNumber(student))
                .thenReturn(studentResult.getCurrentQuestionNumber());
        Mockito.when(questionService.getQuestion(studentResult.getCurrentQuestionNumber()))
                .thenReturn(question);
        Question actual = testingService.getNextQuestion(student);
        Assertions.assertEquals(question, actual);
    }

    @Test
    void getNextQuestionException() {
        Student student = new Student("Bob");
        StudentResult studentResult = new StudentResult(student.getName());
        studentResult.incrementCurrentQuestionNumber();
        studentResult.incrementCurrentQuestionNumber();
        Mockito.when(resultService.getStudentResult(student)).thenReturn(studentResult);
        Mockito.when(questionService.getTestSize()).thenReturn(2);
        Mockito.verify(resultService, Mockito.never()).getCurrentQuestionNumber(student);
        Mockito.verify(questionService, Mockito.never()).getQuestion(studentResult.getCurrentQuestionNumber());
        Assertions.assertThrows(TestFinishedException.class, () -> testingService.getNextQuestion(student));
    }
}