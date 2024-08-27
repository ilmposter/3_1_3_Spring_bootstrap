package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.configs.PasswordEncoder;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@Transactional
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping()
    public String readAllUsers(Principal principal, Model model) {
        model.addAttribute("users", userService.readAllUsers());
        model.addAttribute("currentUser", userService.findByUsername(principal.getName()));
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAll());
        return "admins_page";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("users", new User());
        model.addAttribute("roles", roleService.findAll());
        return "create";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("users") User user,
                             BindingResult bindingResult,
                             @RequestParam("role") String selectedRole) {
        if (bindingResult.hasErrors()) {
            return "create";
        }
        Set<Role> roles;
        if ("ROLE_USER".equals(selectedRole)) {
            roles = roleService.findByName("ROLE_USER").stream().collect(Collectors.toSet());
        } else if ("ROLE_ADMIN".equals(selectedRole)) {
            roles = roleService.findAll().stream().collect(Collectors.toSet());
        } else {
            roles = Set.of(); // Пустой набор ролей, если роль не определена
        }
        user.setRoles(roles);
        userService.createUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.readUserById(id));
        model.addAttribute("roles", roleService.findAll());
        return "update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @ModelAttribute("user") User user,
                             BindingResult bindingResult,
                             @RequestParam("role") String selectedRole) {
        if (bindingResult.hasErrors()) {
            return "update";
        }
// Получаем существующего пользователя из базы данных
        User existingUser = userService.readUserById(id);

        // Обновляем поля существующего пользователя, кроме пароля
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());

        // Если поле пароля не пустое, обновляем пароль
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(user.getPassword())); // Кодируем новый пароль
        }

        Set<Role> roles;
        if ("ROLE_USER".equals(selectedRole)) {
            roles = roleService.findByName("ROLE_USER").stream().collect(Collectors.toSet());
        } else if ("ROLE_ADMIN".equals(selectedRole)) {
            roles = roleService.findAll().stream().collect(Collectors.toSet());
        } else {
            roles = Set.of(); // Пустой набор ролей, если роль не определена
        }
        user.setRoles(roles);
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.readUserById(id));
        return "delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
