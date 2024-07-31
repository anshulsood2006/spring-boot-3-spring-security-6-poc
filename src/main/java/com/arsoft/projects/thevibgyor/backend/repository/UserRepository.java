package com.arsoft.projects.thevibgyor.backend.repository;

import com.arsoft.projects.thevibgyor.backend.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    User findByUsername(String username);
}
