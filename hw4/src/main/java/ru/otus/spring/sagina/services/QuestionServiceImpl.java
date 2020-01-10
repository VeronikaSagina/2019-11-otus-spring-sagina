package ru.otus.spring.sagina.services;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dao.CsvReaderDao;
import ru.otus.spring.sagina.domain.Answer;
import ru.otus.spring.sagina.domain.Question;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final CsvReaderDao csvReaderDao;

    public QuestionServiceImpl(CsvReaderDao csvReaderDao) {
        this.csvReaderDao = csvReaderDao;
    }

    @Override
    public Question getQuestion(int questionNumber) {
        return csvReaderDao.getTestItem(questionNumber).getQuestion();
    }

    @Override
    public Answer getAnswer(int questionNumber) {
        return csvReaderDao.getTestItem(questionNumber).getAnswer();
    }

    @Override
    public int getTestSize() {
        return csvReaderDao.getSize();
    }
}
