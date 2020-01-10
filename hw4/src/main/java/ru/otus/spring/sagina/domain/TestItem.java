package ru.otus.spring.sagina.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TestItem {
    private final Question question;
    private final Answer answer;
}
