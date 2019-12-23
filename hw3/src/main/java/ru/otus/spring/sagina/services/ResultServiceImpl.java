package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dao.StudentResultDao;
import ru.otus.spring.sagina.domain.Student;
import ru.otus.spring.sagina.domain.StudentAnswer;
import ru.otus.spring.sagina.domain.StudentResult;
import ru.otus.spring.sagina.exceptions.NotFoundException;

@Service
public class ResultServiceImpl implements ResultService {
    private final ValidationService validationService;
    private final StudentResultDao studentResultDao;

    public ResultServiceImpl(ValidationService validationService,
                             StudentResultDao studentResultDao) {
        this.validationService = validationService;
        this.studentResultDao = studentResultDao;
    }

    @Override
    public StudentResult create(Student student) {
        StudentResult studentResult = new StudentResult(student.getName());
        studentResultDao.saveStudentResult(studentResult);
        return studentResult;
    }

    @Override
    public void updateResult(StudentAnswer studentAnswer) {
        StudentResult result = get(studentAnswer.getStudent());
        result.incrementCurrentQuestionNumber();
        if (validationService.isCorrectAnswer(studentAnswer)) {
            result.incrementCorrectCount();
        }
    }

    @Override
    public StudentResult get(Student student) {
        return studentResultDao.findByStudent(student)
                .orElseThrow(() -> new NotFoundException("Not found result for student"));
    }

    @Override
    public int getCurrentQuestionNumber(Student student) {
        return get(student).getCurrentQuestionNumber();
    }
}
