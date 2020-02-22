package ru.otus.spring.sagina.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "book")
public class Book {
    @Id
    private String id;

    private String title;

    private Author author;
    @DBRef
    private List<Genre> genres = new ArrayList<>();
    @Transient
    private List<BookComment> comments = new ArrayList<>();
}
