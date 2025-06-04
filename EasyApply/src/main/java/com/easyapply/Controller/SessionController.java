package com.easyapply.Controller;

import com.easyapply.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;

@RestController

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class SessionController {

    @Autowired
    private JwtService jwtService;

    @GetMapping("/api/check-session")
    public ResponseEntity<?> checkSession(HttpServletRequest request) {
        String token = null;
        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split("; ");
            for (String cookie : cookies) {
                if (cookie.startsWith("jwtToken=")) {
                    token = cookie.substring("jwtToken=".length());
                    break;
                }
            }
        }

        if (token == null) {
            return ResponseEntity.status(401).body(Map.of("error", "No session found"));
        }

        try {
           
            Map<String, Object> claims = jwtService.extractClaims(token);
            String role = (String) claims.get("role");
            Long id = ((Number) claims.get("id")).longValue();

            Map<String, Object> response = new HashMap<>();
            response.put("id", id);

            response.put("role", role);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid session: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("jwtToken", "")
                .httpOnly(true)
                .secure(false) // Set to true in production with HTTPS
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(Map.of("message", "Logged out successfully"));
    }
}