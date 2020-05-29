package ru.otus.spring.sagina.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.InputStream;

@Getter
@AllArgsConstructor
public class FileResultDto {
    private String fileName;
    private String contentType;
    private InputStream content;
}
