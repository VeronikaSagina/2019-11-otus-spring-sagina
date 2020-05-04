package ru.otus.spring.sagina.enums;

public enum Type {
    AUTHORS("authors"),
    BOOKS("books"),
    COMMENTS("bookComments"),
    GENRES("genres");
    private String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
