package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.domain.Question;
import ru.otus.spring.sagina.domain.Student;
import ru.otus.spring.sagina.domain.StudentAnswer;
import ru.otus.spring.sagina.domain.StudentResult;

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

    public void testing(Student student) {
        StudentResult studentResult = resultService.create(student);
        while (!isTestFinished(studentResult)) {
            int questionNumber = resultService.getCurrentQuestionNumber(student);
            StudentAnswer studentAnswer = getStudentAnswer(student, questionNumber);
            resultService.updateResult(studentAnswer);
        }
    }

    private StudentAnswer getStudentAnswer(Student student, int questionNumber) {
        Integer choice;
        do {
            askQuestion(questionNumber);
            choice = readChoice();
        } while (choice == null);
        return new StudentAnswer(student, questionNumber, choice);
    }

    private void askQuestion(int questionNumber) {
        Question question = questionService.getQuestion(questionNumber);
        messageService.writeMessage(question.getQuestion());
        question.getVariants()
                .forEach(messageService::writeMessage);
    }

    private Integer readChoice() {
        try {
            String answer = messageService.readMessage();
            return Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            messageService.writeMessage(messageService.getMessage("wrong_format"));
            return null;
        }
    }

    private boolean isTestFinished(StudentResult studentResult) {
        return studentResult.getCurrentQuestionNumber() == questionService.getTestSize();
    }
}