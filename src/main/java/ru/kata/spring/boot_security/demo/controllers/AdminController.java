package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String readAllUsers(Principal principal, Model model) {
        model.addAttribute("users", userService.readAllUsers());
        model.addAttribute("currentUser", userService.findByUsername(principal.getName()));
        return "admins_page";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("users", new User());
        model.addAttribute("roles", roleService.findAll());
        return "create";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("users") @Valid User user,
                             BindingResult bindingResult,
                             @RequestParam("role") String selectedRole) {
        if (bindingResult.hasErrors()) {
            return "create";
        }
        if ("ROLE_USER".equals(selectedRole)) {
            user.setRoles(roleService.findByName("ROLE_USER"));
        } else if ("ROLE_ADMIN".equals(selectedRole)) {
            user.setRoles(roleService.findAll());
        }
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
                             @ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult,
                             @RequestParam("role") String selectedRole) {
        if (bindingResult.hasErrors()) {
            return "update";
        }
        if ("ROLE_USER".equals(selectedRole)) {
            user.setRoles(roleService.findByName("ROLE_USER"));
        } else if ("ROLE_ADMIN".equals(selectedRole)) {
            user.setRoles(roleService.findAll());
        }
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
