package ru.otus.spring.sagina.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class AuthUser {
    private final UUID id;
    private final String login;
}
