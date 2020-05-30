package ru.otus.spring.sagina.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.dto.response.AuthUserResponseDto;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthenticationService(AuthenticationManager authenticationManager,
                                 JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    public AuthUserResponseDto authenticate(String login, String password) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            login,
                            password
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Логин или пароль указаны неверно", ex);
        } catch (AuthenticationException ex) {
            throw new RuntimeException("Ошибка аутентификации", ex);
        }
        Object principal = authentication.getPrincipal();
        var userDetails = (UserDetailsImpl) principal;
        String token = jwtTokenService.createToken(userDetails.getId(), userDetails.getUsername(), userDetails.getAuthorities());
        return new AuthUserResponseDto(token, userDetails.getRole());
    }
}
