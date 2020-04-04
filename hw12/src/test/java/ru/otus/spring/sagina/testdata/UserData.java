package ru.otus.spring.sagina.testdata;

import ru.otus.spring.sagina.entity.User;
import ru.otus.spring.sagina.enums.UserRole;

import java.time.LocalDateTime;

public class UserData {

    public static final User USER = new User(
            "1",
            "Veronika",
            false,
            LocalDateTime.parse("2020-12-12T00:00:30"),
            "$2a$10$BRfnmxJfwIfISHYx5F6vGOgr.LDy02ovrDs4svdfaXVaOnYQm5/zi",
            UserRole.ROLE_ADMIN);

    public static User newUser(boolean locked) {
       return new User("1",
                "Veronika",
                locked,
                LocalDateTime.parse("2020-12-12T00:00:30"),
                "$2a$10$BRfnmxJfwIfISHYx5F6vGOgr.LDy02ovrDs4svdfaXVaOnYQm5/zi",
                UserRole.ROLE_ADMIN);
    }
}
