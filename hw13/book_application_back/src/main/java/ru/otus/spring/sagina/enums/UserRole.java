package ru.otus.spring.sagina.enums;

public enum UserRole {
    ROLE_USER("пользователь"),
    ROLE_ADMIN("администратор"),
    ROLE_LITTLE_USER("детский пользователь");

    private String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
