package ru.otus.spring.sagina.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.otus.spring.sagina.annotation.NullOrNotBlank;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAuthorDto {
    @NotNull
    @JsonProperty("id")
    private UUID id;
    @NullOrNotBlank
    @JsonProperty("name")
    private String name;
    @NullOrNotBlank
    @JsonProperty("description")
    private String description;
}
