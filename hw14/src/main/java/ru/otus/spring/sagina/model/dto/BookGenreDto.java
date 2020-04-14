package ru.otus.spring.sagina.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BookGenreDto {
    String bookId;
    String genreId;
}
