package com.arsoft.projects.thevibgyor.backend.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class User {
    private String username;
    private String userId;
    private String email;
    private List<Role> roles;
    private String password;
}
