package ru.otus.spring.sagina.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.sagina.domain.Answer;
import ru.otus.spring.sagina.domain.Student;
import ru.otus.spring.sagina.domain.StudentAnswer;

@SpringBootTest
@ActiveProfiles("test")
class ValidationServiceTest {
    @Mock
    private QuestionServiceImpl questionService;
    @InjectMocks
    private ValidationServiceImpl validationService;

    @Test
    void testIsCorrectAnswer() {
        int question = 2;
        int choice = 0;
        final int maxChoice = 5;
        final int correctChoice = 2;
        Mockito.when(questionService.getAnswer(2)).thenReturn(new Answer(2));
        while (choice < maxChoice) {
            StudentAnswer studentAnswer =
                    new StudentAnswer(new Student("Bob"), question, choice);
            System.out.println(question + "+++++++++" + choice + "----" + validationService.isCorrectAnswer(studentAnswer));
            Assertions.assertEquals(choice == correctChoice,
                    validationService.isCorrectAnswer(studentAnswer));
            choice++;
        }
    }
}
