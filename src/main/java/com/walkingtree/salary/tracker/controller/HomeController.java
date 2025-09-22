package com.walkingtree.salary.tracker.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "Welcome! Public page.";
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal OAuth2User user) {
        return "Hello, " + user.getAttribute("name");
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "Admin Dashboard - restricted";
    }
}
