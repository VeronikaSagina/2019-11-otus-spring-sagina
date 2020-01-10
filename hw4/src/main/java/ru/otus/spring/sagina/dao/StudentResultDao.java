package ru.otus.spring.sagina.dao;

import ru.otus.spring.sagina.domain.Student;
import ru.otus.spring.sagina.domain.StudentResult;

import java.util.Optional;

public interface StudentResultDao {
    StudentResult saveStudentResult(StudentResult studentResult);

    Optional<StudentResult> findByStudent(Student student);
}
