package ru.otus.spring.sagina.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentAnswer {
    private final Student student;
    private final int question;
    private final int choice;
}
