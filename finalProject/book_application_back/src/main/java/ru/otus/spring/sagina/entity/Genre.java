package ru.otus.spring.sagina.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @GeneratedValue
    @Column(name = "genre_id")
    private UUID id;

    @Column(name = "name")
    private String name;
}

