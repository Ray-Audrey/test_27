package com.example.scholarship.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final String DEMO_USERNAME = "admin";
    private static final String DEMO_PASSWORD = "password123";

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body, HttpSession session) {
        String username = body.get("username");
        String password = body.get("password");

        if (!DEMO_USERNAME.equals(username) || !DEMO_PASSWORD.equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        session.setAttribute("username", username);
        return Map.of("username", username, "message", "Login successful");
    }

    @GetMapping("/me")
    public Map<String, String> me(HttpSession session) {
        Object username = session.getAttribute("username");
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
        }
        return Map.of("username", username.toString());
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpSession session) {
        session.invalidate();
        return Map.of("message", "Logged out");
    }
}
