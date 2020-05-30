package ru.otus.spring.sagina.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.otus.spring.sagina.dto.response.AuthorDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookDto {
    @JsonProperty("id")
    private UUID id;
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
    private List<UUID> genreIds;
}
