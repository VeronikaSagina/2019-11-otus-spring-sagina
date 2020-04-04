package ru.otus.spring.sagina.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;
@Getter
@AllArgsConstructor
public class BookDtoRequest {
    private String id;
    @NotBlank
    private String title;
    @NotBlank
    private String authorId;
    @NotBlank
    private String description;
    private List<String> genreIds;
}
