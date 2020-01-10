package ru.otus.spring.sagina.services;

import ru.otus.spring.sagina.domain.Answer;
import ru.otus.spring.sagina.domain.Question;

public interface QuestionService {
    Question getQuestion(int questionNumber);

    Answer getAnswer(int questionNumber);

    int getTestSize();
}
