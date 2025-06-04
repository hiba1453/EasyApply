package com.easyapply.Controller;


import java.time.LocalDate;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.easyapply.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.easyapply.Repository.CandidatRepository;

import com.easyapply.entity.*;


import com.easyapply.DTO.*;
import com.easyapply.Repository.*;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;  // ← Import important !

import com.easyapply.entity.*;
import com.easyapply.DTO.*; // Assure-toi que ce DTO existe
import io.swagger.v3.oas.annotations.Operation;  // ← Import important !
import io.swagger.v3.oas.annotations.tags.Tag;
import com.easyapply.Service.*;
import com.easyapply.Repository.*;

@RestController

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Tag(name = "Authentication", description = "API d'authentification et gestion des comptes")
public class AuthController {
  
   
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthService authService; // Assure-toi que ce service existe
  
    @Autowired
    private CandidatRepository candidatRepository; // Assure-toi que ce repository existe

    @Autowired
    private CandidatService candidatService;
    @Autowired
    private OffreEmploiService offreEmploiService; // Assure-toi que ce service existe
    @Autowired
    private EntrepriseService entrepriseService;
     @Autowired
    private JobService jobService; // Assure-toi que ce service existe

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

          
            LocalDate dateNaissance;
            try {
                dateNaissance = LocalDate.parse(request.getDateNaissance());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Format de date de naissance invalide (attendu: YYYY-MM-DD)"));
            }

            // Utilise le service pour enregistrer le candidat
            candidatService.registerCandidat(request);

            return ResponseEntity.ok(Map.of("message", "Inscription réussie !"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erreur lors de l'inscription: " + e.getMessage()));
        }
    }
    
    

@PostMapping("/register/company")
@Operation(summary = "Inscription d'une entreprise", description = "Créer un nouveau compte entreprise sur EasyApply")
public ResponseEntity<?> registerCompany(@RequestBody RegisterCRequest request) {
    try {
        // Validation
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email est requis"));
        }
        if (request.getMotDePasse() == null || request.getMotDePasse().length() < 8) {
            return ResponseEntity.badRequest().body(Map.of("error", "Le mot de passe doit contenir au moins 8 caractères"));
        }
        if (request.getNom() == null || request.getNom().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Le nom de l'entreprise est requis"));
        }
        if (request.getSecteur() == null || request.getSecteur().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Le secteur d'activité est requis"));
        }

        Entreprise entreprise = entrepriseService.registerEntreprise(request);

        return ResponseEntity.ok(Map.of(
            "message", "Inscription entreprise réussie !",
            "entrepriseId", entreprise.getId()
        ));
    } catch (Exception e) {
        return ResponseEntity.internalServerError().body(Map.of("error", "Erreur lors de l'inscription: " + e.getMessage()));
    }
}
    
    @PostMapping("/login")
    @Operation(summary = "Connexion d'un utilisateur", description = "Authentifier un utilisateur sur EasyApply")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Validation
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email est requis"));
            }
            if (request.getMotDePasse() == null || request.getMotDePasse().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Mot de passe est requis"));
            }

            // Authentification via le service
            LoginResponse response = authService.authenticate(request);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la connexion: " + e.getMessage()));
        }
    }
    
    @GetMapping("/offres")
    @Operation(summary = "Récupérer les offres d'emploi", description = "Obtenir la liste des offres d'emploi disponibles")
    public ResponseEntity<List<OffreEmploi>> getAllOffres() {
        try {
            List<OffreEmploi> offres = offreEmploiService.getAllOffres();
            return ResponseEntity.ok(offres);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(null); // Or use a custom error response: Map.of("error", e.getMessage())
        }
    }

    
    

   
}