package ru.otus.spring.sagina.entity;

import java.util.List;

public class TestItem {

    private String question;

    private int answer;

    private List<String> variants;

    public TestItem() {
    }

    public TestItem(String question, int answer, List<String> variants) {
        this.question = question;
        this.answer = answer;
        this.variants = variants;
    }

    public String getQuestion() {
        return question;
    }

    public int getAnswer() {
        return answer;
    }

    public List<String> getVariants() {
        return variants;
    }
}
