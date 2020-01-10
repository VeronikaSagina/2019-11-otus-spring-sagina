package ru.otus.spring.sagina.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Question {
    private final String question;
    private final List<String> variants;

    @Override
    public String toString() {
        return  question + '\n' +
                String.join("\n", variants);
    }
}
