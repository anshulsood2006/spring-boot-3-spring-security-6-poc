package com.arsoft.projects.thevibgyor.backend.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @PostMapping(value = "/page")
    public String userPage() {
        log.info("Request has reached user controller now");
        return "You are on user page.";
    }
}
