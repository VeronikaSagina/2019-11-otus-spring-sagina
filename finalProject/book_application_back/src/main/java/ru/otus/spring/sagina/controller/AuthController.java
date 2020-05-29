package ru.otus.spring.sagina.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.sagina.dto.request.AuthUserDto;
import ru.otus.spring.sagina.dto.request.CreateUserDto;
import ru.otus.spring.sagina.dto.response.AuthUserResponseDto;
import ru.otus.spring.sagina.security.AuthenticationService;
import ru.otus.spring.sagina.services.UserService;

@Api(tags = "AuthController")
@RestController
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AuthController(AuthenticationService authenticationService,
                          UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/auth")
    @ApiOperation("авторизация")
    public AuthUserResponseDto auth(@RequestBody AuthUserDto authUserDto) {
        return authenticationService.authenticate(authUserDto.getLogin(), authUserDto.getPassword());
    }

    @PostMapping("/auth/first-login")
    @ApiOperation("регистрация и авторизация")
    public AuthUserResponseDto firstLogin(@RequestBody CreateUserDto user) {
        userService.create(user);
        return authenticationService.authenticate(user.getLogin(), user.getPassword());
    }
}
