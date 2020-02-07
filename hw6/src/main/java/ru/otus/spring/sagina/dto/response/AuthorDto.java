package ru.otus.spring.sagina.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthorDto {
    public final int id;
    public final String name;

    @Override
    public String toString() {
        return "(id=" + id + ", name='" + name + "')";
    }
}
