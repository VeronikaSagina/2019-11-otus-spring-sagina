package ru.otus.spring.sagina.dao;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.domain.Student;
import ru.otus.spring.sagina.domain.StudentResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentResultDaoImpl implements StudentResultDao {
    public Map<String, StudentResult> results = new HashMap<>();

    @Override
    public StudentResult saveStudentResult(StudentResult studentResult) {
        results.put(studentResult.getName(), studentResult);
        return studentResult;
    }

    @Override
    public Optional<StudentResult> findByStudent(Student student) {
        return Optional.ofNullable(results.get(student.getName()));
    }
}
