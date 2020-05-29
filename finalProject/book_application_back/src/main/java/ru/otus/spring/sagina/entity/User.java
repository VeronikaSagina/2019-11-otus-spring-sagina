package ru.otus.spring.sagina.entity;

import lombok.*;
import ru.otus.spring.sagina.enums.UserRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private UUID id;

    @Column(name = "login")
    private String login;

    @Column(name = "email")
    private String email;

    @Column(name = "locked")
    private boolean isLocked;

    @Column(name = "password")
    private String password;

    @Column(name = "consent_to_communication")
    private Boolean consentToCommunication;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @OneToMany(mappedBy = "user")
    private List<BookComment> comments = new ArrayList<>();
}
