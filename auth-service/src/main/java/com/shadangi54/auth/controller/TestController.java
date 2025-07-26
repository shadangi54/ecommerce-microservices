package com.shadangi54.auth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/")
    public Map<String, String> healthCheck() {
        return Collections.singletonMap("status", "UP");
    }

    @GetMapping("/public/test")
    public Map<String, String> publicEndpoint() {
        return Collections.singletonMap("message", "This is a public endpoint that doesn't require authentication");
    }

    @GetMapping("/user-info")
    public Map<String, Object> userInfo(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("username", principal.getName());
    }

    @GetMapping("/admin/test")
    public Map<String, String> adminEndpoint() {
        return Collections.singletonMap("message", "This is an admin-only endpoint");
    }
}
