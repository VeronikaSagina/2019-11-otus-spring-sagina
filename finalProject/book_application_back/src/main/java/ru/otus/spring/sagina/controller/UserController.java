package ru.otus.spring.sagina.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.sagina.dto.mapper.UserDtoMapper;
import ru.otus.spring.sagina.dto.response.UserDto;
import ru.otus.spring.sagina.services.UserService;

import java.util.List;
import java.util.UUID;
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
    @ApiOperation("получить список пользователей")
    public List<UserDto> listUser() {
        return userService.getListUsers().stream()
                .map(userDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/user/{id}/lock")
    @ApiOperation("заблокировать/разблокировать пользователя")
    public UserDto lockUser(@PathVariable("id") UUID userId) {
        return userDtoMapper.toDto(userService.lockUser(userId));
    }

    @PatchMapping("/user")
    @ApiOperation("изменить пользователя")
    public UserDto updateUser(@RequestBody UserDto user) {
        return userDtoMapper.toDto(userService.update(user));
    }
}
