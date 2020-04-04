package ru.otus.spring.sagina.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.spring.sagina.enums.UserRole;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User {
    @Id
    private String id;
    private String login;
    private boolean isLocked;
    private LocalDateTime passwordTimeExpired;
    private String password;
    private UserRole role;
}
