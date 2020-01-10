package ru.otus.spring.sagina.services;

import ru.otus.spring.sagina.domain.Question;
import ru.otus.spring.sagina.domain.Student;

public interface TestingService {
    Question getNextQuestion(Student student);

    void saveAnswer(Student student, int answer);

    String getResult(Student student);

    void restart(Student student);
}
