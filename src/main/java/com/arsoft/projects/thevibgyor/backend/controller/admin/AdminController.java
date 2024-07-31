package com.arsoft.projects.thevibgyor.backend.controller.admin;

import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @PostMapping(value = "/page")
    @RolesAllowed("ADMIN")
    public String adminPage() {
        log.info("Request has reached admin controller now");
        return "You are on admin page.";
    }
}
