package com.walkingtree.salary.tracker.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testIndex_PublicPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome! Public page."));
    }

    @Test
    public void testHome_AuthenticatedUser() throws Exception {
        OAuth2User user = new DefaultOAuth2User(
                Set.of(new SimpleGrantedAuthority("ROLE_USER")),
                Map.of("name", "Test User"),
                "name"
        );

        mockMvc.perform(get("/home").with(oauth2Login().oauth2User(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Test User"));
    }

    @Test
    public void testAdminDashboard_AccessDeniedForUnauthenticated() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().is3xxRedirection());  // Redirect to login or 401 depending on config
    }

    @Test
    public void testAdminDashboard_AllowedForAdmin() throws Exception {
        OAuth2User adminUser = new DefaultOAuth2User(
                Set.of(new SimpleGrantedAuthority("ROLE_ADMIN")),
                Map.of("name", "Admin User"),
                "name"
        );

        mockMvc.perform(get("/admin/dashboard").with(oauth2Login().oauth2User(adminUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("Admin Dashboard - restricted"));
    }

    @Test
    public void testAdminDashboard_ForbiddenForNonAdmin() throws Exception {
        OAuth2User regularUser = new DefaultOAuth2User(
                Set.of(new SimpleGrantedAuthority("ROLE_USER")),
                Map.of("name", "Regular User"),
                "name"
        );

        mockMvc.perform(get("/admin/dashboard").with(oauth2Login().oauth2User(regularUser)))
                .andExpect(status().isForbidden());
    }
}

