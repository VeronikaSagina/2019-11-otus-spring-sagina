package ru.otus.spring.sagina.dto.request;

import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;
@AllArgsConstructor
public class BookDtoRequest {
    public final String id;
    @NotBlank
    public final String title;
    @NotBlank
    public final String authorId;
    @NotBlank
    public final String description;
    public final List<String> genreIds;
}
