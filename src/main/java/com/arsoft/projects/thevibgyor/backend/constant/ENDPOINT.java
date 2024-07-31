package com.arsoft.projects.thevibgyor.backend.constant;

import lombok.Getter;

@Getter
public enum ENDPOINT {

    GET_TOKEN_ENDPOINT("/auth/get-token"),
    ADMIN_ENDPOINTS("/admin"),
    LOGGED_USER_ENDPOINTS("/user"),
    ALL_ENDPOINTS("/*");

    private final String value;

    ENDPOINT(String value) {
        this.value = value;
    }

}
