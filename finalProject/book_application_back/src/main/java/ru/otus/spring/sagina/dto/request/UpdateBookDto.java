package ru.otus.spring.sagina.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.otus.spring.sagina.annotation.NullOrNotBlank;
import ru.otus.spring.sagina.dto.response.AuthorDto;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookDto {
    @JsonProperty("id")
    private UUID id;
    @NullOrNotBlank
    @JsonProperty("name")
    private String name;
    @NullOrNotBlank
    @JsonProperty("description")
    private String description;
    @JsonProperty("author")
    private AuthorDto author;
    @JsonProperty("genres")
    private List<UUID> genreIds;
}
