package com.arsoft.projects.thevibgyor.backend.model;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ADMIN"),
    GUEST("GUEST"),
    USER("USER");

    private final String value;

    Role(String value) {
        this.value = value;
    }

}
