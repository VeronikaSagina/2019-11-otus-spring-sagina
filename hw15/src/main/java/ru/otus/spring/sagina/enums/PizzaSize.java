package ru.otus.spring.sagina.enums;

import java.util.Random;

public enum PizzaSize {
    M("средняя"),
    S("маленькая"),
    L("большая");

    private static final Random RANDOM = new Random();
    private String value;

    PizzaSize(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PizzaSize getRandomPizzaSize() {
        return values()[RANDOM.nextInt(values().length)];
    }
}
