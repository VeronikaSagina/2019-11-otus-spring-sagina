package ru.otus.spring.sagina.services;

import ru.otus.spring.sagina.domain.StudentAnswer;

public interface ValidationService {
    boolean isCorrectAnswer(StudentAnswer studentAnswer);
}
