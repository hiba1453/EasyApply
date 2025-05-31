import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyapply.service.AuthService;
import com.easyapply.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;  // ← Import important !

import com.easyapply.entity.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Tag(name = "Authentication", description = "API d'authentification et gestion des comptes")
public class AuthController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Inscription d'un nouvel utilisateur", 
               description = "Créer un nouveau compte utilisateur sur EasyApply")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Validation
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email est requis"));
            }
            
            if (request.getPassword() == null || request.getPassword().length() < 6) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Mot de passe doit contenir au moins 6 caractères"));
            }
            if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Prénom est requis"));
        }
        if (request.getLastName() == null || request.getLastName().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Nom est requis"));
        }

            // Créer un utilisateur (simulation - pas encore de base de données)
           User user =userService.createUser(
                request.getEmail(),
             request.getPassword(), 
             request.getFirstName(), 
             request.getLastName());

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

             } catch (RuntimeException e) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", e.getMessage()));
   

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
            // Validation
            if (request.getEmail() == null || request.getPassword() == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email et mot de passe requis"));
            }if (request.getPassword() == null || request.getPassword().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Mot de passe est requis"));
            }

            // ✅ UTILISER LE SERVICE RÉEL POUR L'AUTHENTIFICATION
            
            Map<String, Object> authResult = authService.authenticateUser(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(Map.of(
                "message", "Connexion réussie !",
                "token", authResult.get("token"),
                "user", authResult.get("user")
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.status(401)
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la connexion: " + e.getMessage()));
        }
    }

}
