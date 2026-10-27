package com.example.library.controller;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {
    private final AuthController controller = new AuthController();

    @Test
    void validLoginCreatesSession() {
        MockHttpSession session = new MockHttpSession();
        ResponseEntity<?> result = controller.login(Map.of("username", "admin", "password", "admin123"), session);
        assertEquals(200, result.getStatusCode().value());
        assertEquals("admin", session.getAttribute("username"));
    }

    @Test
    void invalidLoginReturnsUnauthorized() {
        ResponseEntity<?> result = controller.login(Map.of("username", "admin", "password", "wrong"), new MockHttpSession());
        assertEquals(401, result.getStatusCode().value());
    }

    @Test
    void meWithoutSessionReturnsUnauthorized() {
        ResponseEntity<?> result = controller.me(new MockHttpSession());
        assertEquals(401, result.getStatusCode().value());
    }

    @Test
    void meWithSessionReturnsUsername() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "admin");
        ResponseEntity<?> result = controller.me(session);
        assertEquals(200, result.getStatusCode().value());
        assertEquals("admin", ((Map<?, ?>) result.getBody()).get("username"));
    }

    @Test
    void logoutInvalidatesSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "admin");
        ResponseEntity<?> result = controller.logout(session);
        assertEquals(200, result.getStatusCode().value());
        assertThrows(IllegalStateException.class, session::getAttributeNames);
    }
}
