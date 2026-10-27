package com.example.library.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpSession session) {
        if ("admin".equals(body.get("username")) && "admin123".equals(body.get("password"))) {
            session.setAttribute("username", "admin");
            return ResponseEntity.ok(Map.of("username", "admin"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        Object username = session.getAttribute("username");
        if (username == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Not logged in"));
        return ResponseEntity.ok(Map.of("username", username));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }
}
