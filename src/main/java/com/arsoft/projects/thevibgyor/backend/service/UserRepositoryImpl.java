package com.arsoft.projects.thevibgyor.backend.service;

import com.arsoft.projects.thevibgyor.backend.model.Role;
import com.arsoft.projects.thevibgyor.backend.model.User;
import com.arsoft.projects.thevibgyor.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findByUsername(String username) {
        //This is needed, otherwise credentials won't match
        String encodedPassword = passwordEncoder.encode("sood");
        if (username.equals("anshul")) {
            return new User("anshul", "anshul", "anshul", List.of(Role.ADMIN, Role.USER), encodedPassword);
        }
        if (username.equals("akhil")) {
            return new User("akhil", "akhil", "akhil", List.of(Role.USER), encodedPassword);
        }
        return new User("guest", "guest", "guest.com", List.of(Role.GUEST), null);
    }
}