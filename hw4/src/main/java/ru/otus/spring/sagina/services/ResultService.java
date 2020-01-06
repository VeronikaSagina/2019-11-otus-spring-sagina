package ru.otus.spring.sagina.services;

import ru.otus.spring.sagina.domain.Student;
import ru.otus.spring.sagina.domain.StudentAnswer;
import ru.otus.spring.sagina.domain.StudentResult;

public interface ResultService {
    StudentResult create(Student student);

    void updateResult(StudentAnswer studentAnswer);

    StudentResult getStudentResult(Student student);

    int getCurrentQuestionNumber(Student student);
}
