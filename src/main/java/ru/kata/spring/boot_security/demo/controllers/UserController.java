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

    @GetMapping()
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public String readUser(Principal principal, Model model) {
        String username = principal.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "error"; // Либо перенаправление на страницу с ошибкой
        }

        model.addAttribute("user", user);
        return "user"; // Изменяем название представления на 'user', чтобы соответствовать логике
    }
}

