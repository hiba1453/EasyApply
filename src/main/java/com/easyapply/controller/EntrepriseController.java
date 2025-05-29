package com.easyapply.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.easyapply.entity.Entreprise;
import com.easyapply.entity.Job;
import com.easyapply.entity.Application;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/entreprises")
@CrossOrigin(origins = "*")
@Tag(name = "Entreprises", description = "API de gestion des comptes entreprises")
public class EntrepriseController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(summary = "Inscription entreprise", 
               description = "Créer un nouveau compte entreprise")
    public ResponseEntity<?> registerEntreprise(@RequestBody RegisterEntrepriseRequest request) {
        try {
            // Validation basique
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email requis"));
            }
            
            if (request.getPassword() == null || request.getPassword().length() < 6) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Mot de passe doit contenir au moins 6 caractères"));
            }
            
            if (request.getNom() == null || request.getNom().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Nom de l'entreprise requis"));
            }

            // Vérifier si email déjà utilisé (simulation)
            if ("entreprise@test.com".equals(request.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email déjà utilisé"));
            }

            // Créer entreprise (simulation - pas encore de base de données)
            Entreprise entreprise = new Entreprise();
            entreprise.setEmail(request.getEmail());
            entreprise.setPassword(passwordEncoder.encode(request.getPassword()));
            entreprise.setNom(request.getNom());
            entreprise.setSecteur(request.getSecteur());
            entreprise.setSiteWeb(request.getSiteWeb());
            entreprise.setAdresse(request.getAdresse());
            entreprise.setNumeroSiret(request.getNumeroSiret());
            entreprise.setDescription(request.getDescription());
            entreprise.setIsActive(true);

            // Réponse de succès
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Compte entreprise créé avec succès !");
            response.put("entreprise", Map.of(
                "email", entreprise.getEmail(),
                "nom", entreprise.getNom(),
                "secteur", entreprise.getSecteur() != null ? entreprise.getSecteur() : "Non spécifié"
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la création du compte: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion entreprise", 
               description = "Authentification pour les entreprises")
    public ResponseEntity<?> loginEntreprise(@RequestBody LoginEntrepriseRequest request) {
        try {
            // Validation basique
            if (request.getEmail() == null || request.getPassword() == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email et mot de passe requis"));
            }

            // Simulation d'authentification entreprise
            if ("entreprise@test.com".equals(request.getEmail()) && 
                "password123".equals(request.getPassword())) {
                
                // Succès - générer un faux JWT pour l'instant
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Connexion entreprise réussie !");
                response.put("token", "fake-jwt-entreprise-" + System.currentTimeMillis());
                response.put("entreprise", Map.of(
                    "email", request.getEmail(),
                    "nom", "Test Entreprise",
                    "secteur", "Technologie",
                    "role", "ENTREPRISE"
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

    @GetMapping("/{id}/dashboard")
    @Operation(summary = "Dashboard entreprise", 
               description = "Tableau de bord avec statistiques entreprise")
    public ResponseEntity<?> getDashboard(@PathVariable Long id) {
        try {
            // Simulation du dashboard entreprise
            Map<String, Object> dashboard = new HashMap<>();
            
            // Infos entreprise
            Map<String, Object> entrepriseInfo = new HashMap<>();
            entrepriseInfo.put("id", id);
            entrepriseInfo.put("nom", "Test Entreprise");
            entrepriseInfo.put("secteur", "Technologie");
            entrepriseInfo.put("offresActives", 3);
            
            dashboard.put("entreprise", entrepriseInfo);
            
            // Statistiques
            Map<String, Object> stats = new HashMap<>();
            stats.put("offresPubliees", 5);
            stats.put("offresActives", 3);
            stats.put("candidaturesRecues", 23);
            stats.put("candidaturesEnAttente", 12);
            stats.put("candidaturesAcceptees", 8);
            stats.put("candidaturesRefusees", 3);
            
            dashboard.put("statistiques", stats);
            
            return ResponseEntity.ok(dashboard);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la récupération du dashboard: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}/candidatures")
    @Operation(summary = "Candidatures reçues", 
               description = "Liste des candidatures pour toutes les offres de l'entreprise")
    public ResponseEntity<?> getCandidatures(
            @Parameter(description = "ID de l'entreprise") @PathVariable Long id,
            @Parameter(description = "Statut des candidatures") @RequestParam(required = false) String statut) {
        
        try {
            // Simulation des candidatures reçues
            List<Map<String, Object>> candidatures = new ArrayList<>();
            
            // Candidature 1
            Map<String, Object> candidature1 = new HashMap<>();
            candidature1.put("id", 1L);
            candidature1.put("candidatNom", "John Doe");
            candidature1.put("candidatEmail", "john.doe@email.com");
            candidature1.put("offreId", 1L);
            candidature1.put("offreTitre", "Développeur Full Stack Java/React");
            candidature1.put("statut", "PENDING");
            candidature1.put("datePostulation", LocalDateTime.now().minusDays(2));
            candidature1.put("coverLetter", "Motivé par ce poste qui correspond parfaitement à mon profil...");
            
            // Candidature 2
            Map<String, Object> candidature2 = new HashMap<>();
            candidature2.put("id", 2L);
            candidature2.put("candidatNom", "Jane Smith");
            candidature2.put("candidatEmail", "jane.smith@email.com");
            candidature2.put("offreId", 1L);
            candidature2.put("offreTitre", "Développeur Full Stack Java/React");
            candidature2.put("statut", "VIEWED");
            candidature2.put("datePostulation", LocalDateTime.now().minusDays(5));
            candidature2.put("coverLetter", "Expérience solide en développement web moderne...");
            
            candidatures.add(candidature1);
            candidatures.add(candidature2);
            
            // Filtrage par statut si fourni
            if (statut != null && !statut.isEmpty()) {
                candidatures = candidatures.stream()
                    .filter(c -> c.get("statut").toString().equalsIgnoreCase(statut))
                    .toList();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("candidatures", candidatures);
            response.put("total", candidatures.size());
            response.put("filtres", Map.of("statut", statut != null ? statut : "Tous"));
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la récupération des candidatures: " + e.getMessage()));
        }
    }

    @PatchMapping("/candidatures/{candidatureId}/statut")
    @Operation(summary = "Répondre à une candidature", 
               description = "Accepter ou refuser une candidature")
    public ResponseEntity<?> repondreCandidature(
            @Parameter(description = "ID de la candidature") @PathVariable Long candidatureId,
            @RequestBody UpdateCandidatureRequest request) {
        
        try {
            // Validation
            if (request.getStatut() == null || request.getStatut().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Statut requis (ACCEPTED, REJECTED)"));
            }
            
            if (!Arrays.asList("ACCEPTED", "REJECTED").contains(request.getStatut().toUpperCase())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Statut invalide. Utilisez ACCEPTED ou REJECTED"));
            }
            
            // Simulation de mise à jour du statut
            Map<String, Object> candidatureMiseAJour = new HashMap<>();
            candidatureMiseAJour.put("id", candidatureId);
            candidatureMiseAJour.put("statut", request.getStatut().toUpperCase());
            candidatureMiseAJour.put("dateReponse", LocalDateTime.now());
            candidatureMiseAJour.put("commentaire", request.getCommentaire());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Statut de la candidature mis à jour avec succès !");
            response.put("candidature", candidatureMiseAJour);
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la mise à jour: " + e.getMessage()));
        }
    }

    // Classes DTO pour les requêtes
    public static class RegisterEntrepriseRequest {
        private String email;
        private String password;
        private String nom;
        private String secteur;
        private String siteWeb;
        private String adresse;
        private String numeroSiret;
        private String description;

        // Getters et Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
        
        public String getSecteur() { return secteur; }
        public void setSecteur(String secteur) { this.secteur = secteur; }
        
        public String getSiteWeb() { return siteWeb; }
        public void setSiteWeb(String siteWeb) { this.siteWeb = siteWeb; }
        
        public String getAdresse() { return adresse; }
        public void setAdresse(String adresse) { this.adresse = adresse; }
        
        public String getNumeroSiret() { return numeroSiret; }
        public void setNumeroSiret(String numeroSiret) { this.numeroSiret = numeroSiret; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class LoginEntrepriseRequest {
        private String email;
        private String password;

        // Getters et Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class UpdateCandidatureRequest {
        private String statut;
        private String commentaire;

        // Getters et Setters
        public String getStatut() { return statut; }
        public void setStatut(String statut) { this.statut = statut; }
        
        public String getCommentaire() { return commentaire; }
        public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    }
}