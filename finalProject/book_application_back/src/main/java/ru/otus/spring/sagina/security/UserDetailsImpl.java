package ru.otus.spring.sagina.security;

import lombok.Getter;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.otus.spring.sagina.entity.User;
import ru.otus.spring.sagina.enums.UserRole;

import java.util.List;
import java.util.UUID;

public class UserDetailsImpl implements UserDetails  {
    @Getter
    private UUID id;
    @Getter
    private UserRole role;
    private String login;
    private String password;
    private boolean locked;
    private boolean isAccountExpired = false;

    public UserDetailsImpl(User user) {
        this.id = user.getId();
        this.locked = user.isLocked();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isAccountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (!locked) {
            return true;
        }
        throw new LockedException("Пользователь заблокирован");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isAccountExpired;
    }

    @Override
    public boolean isEnabled() {
        return !locked;
    }
}
