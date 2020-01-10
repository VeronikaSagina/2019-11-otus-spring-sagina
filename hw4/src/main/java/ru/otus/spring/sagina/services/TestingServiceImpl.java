package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.domain.Question;
import ru.otus.spring.sagina.domain.Student;
import ru.otus.spring.sagina.domain.StudentAnswer;
import ru.otus.spring.sagina.domain.StudentResult;
import ru.otus.spring.sagina.exceptions.TestFinishedException;
import ru.otus.spring.sagina.exceptions.TestNotFinishedException;

@Service
public class TestingServiceImpl implements TestingService {
    private final MessageService messageService;
    private final ResultService resultService;
    private final QuestionService questionService;

    public TestingServiceImpl(MessageService messageService,
                              ResultService resultService,
                              QuestionService questionService) {
        this.messageService = messageService;
        this.resultService = resultService;
        this.questionService = questionService;
    }

    @Override
    public Question getNextQuestion(Student student) {
        StudentResult studentResult = resultService.getStudentResult(student);
        if (isTestFinished(studentResult)) {
            throw new TestFinishedException("The test is over");
        }
        int questionNumber = resultService.getCurrentQuestionNumber(student);
        return questionService.getQuestion(questionNumber);
    }

    @Override
    public void saveAnswer(Student student, int answer) {
        StudentResult studentResult = resultService.getStudentResult(student);
        StudentAnswer studentAnswer = new StudentAnswer(student, studentResult.getCurrentQuestionNumber(), answer);
        resultService.updateResult(studentAnswer);
    }

    @Override
    public String getResult(Student student) {
        StudentResult result = resultService.getStudentResult(student);
        if (!isTestFinished(result)) {
            throw new TestNotFinishedException("Test is not finished yet");
        }
        return messageService.getMessage(
                "resume",
                new Object[]{result.getName(), result.getCorrectCount(), result.getCurrentQuestionNumber()});
    }

    @Override
    public void restart(Student student) {
        resultService.create(student);
    }

    private boolean isTestFinished(StudentResult studentResult) {
        return studentResult.getCurrentQuestionNumber() == questionService.getTestSize();
    }
}