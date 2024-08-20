package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.Collection;
import java.util.List;

public interface RoleService {

    void addRole(Role role);

    List<Role> findAll();

    Collection<Role> findByName(String name);

}
