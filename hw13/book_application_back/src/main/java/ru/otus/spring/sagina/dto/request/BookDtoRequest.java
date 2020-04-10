package ru.otus.spring.sagina.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.otus.spring.sagina.dto.response.AuthorDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookDtoRequest {
    @JsonProperty("id")
    private Long id;
    @NotBlank
    @JsonProperty("name")
    private String name;
    @NotBlank
    @JsonProperty("description")
    private String description;
    @NotNull
    @JsonProperty("author")
    private AuthorDto author;
    @NotEmpty
    @JsonProperty("genres")
    private List<Long> genreIds;
}
