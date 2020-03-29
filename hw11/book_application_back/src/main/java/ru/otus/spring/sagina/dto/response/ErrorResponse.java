package ru.otus.spring.sagina.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResponse {
    private int code;
    private String message;
}