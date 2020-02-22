package ru.otus.spring.sagina.dto.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor
public class GenreDto {
    public final String id;
    public final String type;

    @Override
    public String toString() {
        return "(id=" + id + ", type='" + type + "')";
    }
}
