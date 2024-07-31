package com.arsoft.projects.thevibgyor.backend.service;

import com.arsoft.projects.thevibgyor.backend.model.CustomUserDetails;
import com.arsoft.projects.thevibgyor.backend.model.User;
import com.arsoft.projects.thevibgyor.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return new CustomUserDetails(user);
    }
}
