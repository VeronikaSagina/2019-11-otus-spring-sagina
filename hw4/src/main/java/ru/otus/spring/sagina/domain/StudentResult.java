package ru.otus.spring.sagina.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class StudentResult {
    private final String name;
    private int correctCount;
    private int currentQuestionNumber;

    public StudentResult(String name) {
        this.name = name;
        correctCount = 0;
        currentQuestionNumber = 0;
    }

    public void incrementCorrectCount() {
        correctCount++;
    }

    public void incrementCurrentQuestionNumber() {
        currentQuestionNumber++;
    }
}
