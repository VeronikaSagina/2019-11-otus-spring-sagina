package ru.otus.spring.sagina.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthorDto {
    public final String id;
    public final String name;

    @Override
    public String toString() {
        return "(id=" + id + ", name='" + name + "')";
    }
}
