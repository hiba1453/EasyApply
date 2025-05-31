import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;  // ← Import important !

import com.easyapply.entity.*;

@RestController

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Tag(name = "Authentication", description = "API d'authentification et gestion des comptes")
public class AuthController {
    @Autowired
    private PasswordEncoder passwordEncoder;
  
    @Autowired
    private CandidatRepository candidatRepository; // Assure-toi que ce repository existe

    @PostMapping("/register")
    @Operation(summary = "Inscription d'un nouvel utilisateur", description = "Créer un nouveau compte utilisateur sur EasyApply")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Validation
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email est requis"));
            }
            if (request.getMotDePasse() == null || request.getMotDePasse().length() < 8) {
                return ResponseEntity.badRequest().body(Map.of("error", "Le mot de passe doit contenir au moins 8 caractères"));
            }
            if (!request.getMotDePasse().equals(request.getConfirmedMotDePasse())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Les mots de passe ne correspondent pas"));
            }
            if (request.getNom() == null || request.getNom().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Le nom est requis"));
            }
            if (request.getTelephone() == null || request.getTelephone().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Le téléphone est requis"));
            }
            if (request.getDateNaissance() == null || request.getDateNaissance().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "La date de naissance est requise"));
            }

            // Parse dateNaissance
            LocalDate dateNaissance;
            try {
                dateNaissance = LocalDate.parse(request.getDateNaissance());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Format de date de naissance invalide (attendu: YYYY-MM-DD)"));
            }

            // Créer et sauvegarder le candidat
            Candidat candidat = new Candidat();
            candidat.setNom(request.getNom());
            candidat.setEmail(request.getEmail());
            candidat.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
            candidat.setTelephone(request.getTelephone());
            candidat.setDateNaissance(dateNaissance);

            candidatRepository.save(candidat); // <-- Sauvegarde ici

            return ResponseEntity.ok(Map.of("message", "Inscription réussie !"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erreur lors de l'inscription: " + e.getMessage()));
        }
    }

   
}
