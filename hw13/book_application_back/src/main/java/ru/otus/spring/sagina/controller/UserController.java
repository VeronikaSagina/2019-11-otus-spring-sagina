package ru.otus.spring.sagina.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.sagina.dto.mapper.UserDtoMapper;
import ru.otus.spring.sagina.dto.response.UserDto;
import ru.otus.spring.sagina.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "UserController")
@RestController
public class UserController {
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    public UserController(UserService userService,
                          UserDtoMapper userDtoMapper) {
        this.userService = userService;
        this.userDtoMapper = userDtoMapper;
    }

    @GetMapping("/user")
    @ApiOperation("получение списка пользователей")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserDto> listUser() {
        return userService.getListUsers().stream()
                .map(userDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/user/{id}/delete")
    @ApiOperation("удаление пользователя")
    @PreAuthorize("hasPermission(#userId, 'ru.otus.spring.sagina.entity.User', 'DELETE')")
    public void deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
    }

    @PostMapping("/user/{id}/lock")
    @ApiOperation("заблокировать/разблокировать пользователя")
    @PreAuthorize("hasPermission(#userId, 'ru.otus.spring.sagina.entity.User', 'WRITE')")
    public UserDto lockUser(@PathVariable("id") Long userId) {
        return userDtoMapper.toDto(userService.lockUser(userId));
    }
}
