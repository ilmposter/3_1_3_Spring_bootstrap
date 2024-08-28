package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;


@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String readUser(Principal principal, Model model) {
        User currentUser = userService.findByUsername(principal.getName());

        if (currentUser == null) {
            return "error"; // Либо перенаправление на страницу с ошибкой
        }

        model.addAttribute("currentUser", currentUser);
        return "user"; // Изменяем название представления на 'user', чтобы соответствовать логике
    }
}

