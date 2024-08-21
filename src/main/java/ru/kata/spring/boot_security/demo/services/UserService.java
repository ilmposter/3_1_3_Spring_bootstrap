package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.Collection;
import java.util.List;

@Service
public interface UserService extends UserDetailsService {

    User findByUsername(String username);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles);

    void createUser(User user);

    User readUserById(Long id);

    List<User> readAllUsers();

    void updateUser(Long id, User user);

    void deleteUser(Long id);
}
