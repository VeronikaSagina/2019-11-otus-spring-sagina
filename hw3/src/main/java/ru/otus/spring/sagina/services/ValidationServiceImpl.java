package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.domain.StudentAnswer;

@Service
public class ValidationServiceImpl implements ValidationService {
    private final QuestionService questionService;

    public ValidationServiceImpl(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    public boolean isCorrectAnswer(StudentAnswer studentAnswer) {
        return questionService.getAnswer(studentAnswer.getQuestion())
                .getCorrectChoice() == studentAnswer.getChoice();
    }
}