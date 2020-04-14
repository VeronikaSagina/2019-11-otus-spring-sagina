package ru.otus.spring.sagina.testdata;

import ru.otus.spring.sagina.entity.User;
import ru.otus.spring.sagina.enums.UserRole;

public class UserData {
    public static final User ADMIN_VERONIKA = new User(
            1L,
            "Veronika",
            false,
            "$2a$10$BRfnmxJfwIfISHYx5F6vGOgr.LDy02ovrDs4svdfaXVaOnYQm5/zi",
            UserRole.ROLE_ADMIN);

    public static final User ADMIN_VASYA = new User(
            2L,
            "Vasya",
            false,
            "$2a$10$BRfnmxJfwIfISHYx5F6vGOgr.LDy02ovrDs4svdfaXVaOnYQm5/zi",
            UserRole.ROLE_ADMIN);

    public static final User USER_VICTOR = new User(
            3L,
            "Victor",
            true,
            "$2a$10$BRfnmxJfwIfISHYx5F6vGOgr.LDy02ovrDs4svdfaXVaOnYQm5/zi",
            UserRole.ROLE_USER);

    public static final User USER_NASTYA = new User(
            4L,
            "Nastya",
            false,
            "$2a$10$BRfnmxJfwIfISHYx5F6vGOgr.LDy02ovrDs4svdfaXVaOnYQm5/zi",
            UserRole.ROLE_LITTLE_USER);

    public static User newUserNastya(boolean locked) {
        return new User(4L,
                "Nastya",
                locked,
                "$2a$10$BRfnmxJfwIfISHYx5F6vGOgr.LDy02ovrDs4svdfaXVaOnYQm5/zi",
                UserRole.ROLE_LITTLE_USER);
    }
}
