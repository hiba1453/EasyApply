package com.easyapply.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;  // ← Import important !

import com.easyapply.entity.User;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "API d'authentification et gestion des comptes")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(summary = "Inscription d'un nouvel utilisateur", 
               description = "Créer un nouveau compte utilisateur sur EasyApply")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Validation basique
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email est requis"));
            }
            
            if (request.getPassword() == null || request.getPassword().length() < 6) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Mot de passe doit contenir au moins 6 caractères"));
            }

            // Créer un utilisateur (simulation - pas encore de base de données)
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setRole(User.Role.CANDIDATE);
            user.setIsActive(true);

            // Réponse de succès
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Inscription réussie !");
            response.put("user", Map.of(
                "email", user.getEmail(),
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "role", user.getRole()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de l'inscription: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion utilisateur", 
               description = "Se connecter avec email et mot de passe")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Validation basique
            if (request.getEmail() == null || request.getPassword() == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email et mot de passe requis"));
            }

            // Simulation d'authentification (remplacer par vraie logique plus tard)
            if ("test@easyapply.com".equals(request.getEmail()) && 
                "password123".equals(request.getPassword())) {
                
                // Succès - générer un faux JWT pour l'instant
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Connexion réussie !");
                response.put("token", "fake-jwt-token-" + System.currentTimeMillis());
                response.put("user", Map.of(
                    "email", request.getEmail(),
                    "firstName", "Test",
                    "lastName", "User",
                    "role", "CANDIDATE"
                ));

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401)
                    .body(Map.of("error", "Email ou mot de passe incorrect"));
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la connexion: " + e.getMessage()));
        }
    }

    @GetMapping("/status")
    @Operation(summary = "Statut de l'API", 
               description = "Vérifier que l'API d'authentification fonctionne")
    public ResponseEntity<?> status() {
        return ResponseEntity.ok(Map.of(
            "status", "OK",
            "service", "EasyApply Authentication API",
            "timestamp", LocalDateTime.now(),
            "version", "1.0.0"
        ));
    }

    // Classes DTO pour les requêtes
    public static class RegisterRequest {
        private String email;
        private String password;
        private String firstName;
        private String lastName;

        // Getters et Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
    }

    public static class LoginRequest {
        private String email;
        private String password;

        // Getters et Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}