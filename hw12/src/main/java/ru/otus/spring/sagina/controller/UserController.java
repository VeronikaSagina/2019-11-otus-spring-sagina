package ru.otus.spring.sagina.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.spring.sagina.dto.mapper.UserDtoMapper;
import ru.otus.spring.sagina.dto.response.UserDto;
import ru.otus.spring.sagina.security.SecurityUserDetails;
import ru.otus.spring.sagina.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    public UserController(UserService userService,
                          UserDtoMapper userDtoMapper) {
        this.userService = userService;
        this.userDtoMapper = userDtoMapper;
    }

    @GetMapping("/user")
    public String listUser(Model model) {
        List<UserDto> users = userService.getListUsers().stream()
                .map(userDtoMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("users", users);
        return "users";
    }

    @PostMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable("id") String id) {
        validateOperation(id);
        userService.deleteUser(id);
        return "redirect:/user";
    }

    @PostMapping("/user/{id}/lock")
    public String lockUser(@PathVariable("id") String id) {
        validateOperation(id);
        userService.lockUser(id);
        return "redirect:/user";
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    private void validateOperation(String id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof SecurityUserDetails) {
            SecurityUserDetails userDetails = (SecurityUserDetails) principal;
            if (userDetails.getId().equals(id)) {
                throw new UnsupportedOperationException("Не допустимая операция");
            }
        }
    }
}
