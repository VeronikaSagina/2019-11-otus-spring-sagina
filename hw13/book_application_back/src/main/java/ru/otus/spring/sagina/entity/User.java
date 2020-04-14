package ru.otus.spring.sagina.entity;

import lombok.*;
import ru.otus.spring.sagina.enums.UserRole;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "login")
    private String login;
    @Column(name = "locked")
    private boolean isLocked;
    @Column(name = "password")
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;
}
