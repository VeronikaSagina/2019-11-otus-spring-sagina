package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.sagina.dao.StudentResultDao;
import ru.otus.spring.sagina.domain.Student;
import ru.otus.spring.sagina.domain.StudentAnswer;
import ru.otus.spring.sagina.domain.StudentResult;

import java.util.Optional;

@SpringBootTest
class ResultServiceTest {
    @Mock
    private StudentResultDao studentResultDao;
    @Mock
    private ValidationServiceImpl validationService;
    @InjectMocks
    private ResultServiceImpl resultService;

    @Test
    void testCreate() {
        Student student = new Student("Vasya");
        StudentResult studentResult = resultService.create(student);
        Mockito.verify(studentResultDao).saveStudentResult(Mockito.any());
        Assertions.assertEquals(new StudentResult(student.getName()), studentResult);
    }

    @Test
    void testUpdateResult() {
        Student student = new Student("Bob's brother");
        StudentResult studentResult = new StudentResult(student.getName());
        Mockito.when(studentResultDao.findByStudent(student)).thenReturn(Optional.of(studentResult));
        Mockito.when(validationService.isCorrectAnswer(Mockito.any())).thenReturn(true);
        StudentAnswer studentAnswer = new StudentAnswer(student, 1, 2);
        StudentResult expected = new StudentResult(student.getName());
        expected.incrementCorrectCount();
        expected.incrementCurrentQuestionNumber();
        resultService.updateResult(studentAnswer);
        Assertions.assertEquals(expected, studentResult);

        expected.incrementCurrentQuestionNumber();
        Mockito.when(validationService.isCorrectAnswer(Mockito.any())).thenReturn(false);
        resultService.updateResult(studentAnswer);
        Assertions.assertEquals(expected, studentResult);
    }

    @Test
    void testGet() {
        Student student = new Student("Bob");
        StudentResult expected = new StudentResult(student.getName());
        expected.incrementCorrectCount();
        expected.incrementCurrentQuestionNumber();
        Mockito.when(studentResultDao.findByStudent(student)).thenReturn(Optional.of(expected));
        StudentResult actual = resultService.getStudentResult(student);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testGetCurrentQuestionNumber() {
        Student student = new Student("Tom");
        StudentResult expected = new StudentResult(student.getName());
        expected.incrementCurrentQuestionNumber();
        expected.incrementCurrentQuestionNumber();
        Mockito.when(studentResultDao.findByStudent(student)).thenReturn(Optional.of(expected));
        int userTestPosition = resultService.getCurrentQuestionNumber(student);
        Assertions.assertEquals(expected.getCurrentQuestionNumber(), userTestPosition);
    }
}