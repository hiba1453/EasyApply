package com.easyapply.controller;

import com.easyapply.entity.User;
import com.easyapply.entity.Profile;
import com.easyapply.service.LinkedInService;
import com.easyapply.service.UserService;
import com.easyapply.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@RestController
@RequestMapping("/api/linkedin")
@CrossOrigin(origins = "*")
@Tag(name = "LinkedIn Integration", description = "Intégration LinkedIn OAuth2 réelle")
public class LinkedInController {

    @Autowired
    private LinkedInService linkedInService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthService authService;

    @PostMapping("/import-profile")
    @Operation(summary = "Importer profil LinkedIn", 
               description = "Créer un utilisateur à partir d'un profil LinkedIn réel")
    public ResponseEntity<?> importLinkedInProfile(@RequestBody LinkedInImportRequest request) {
        try {
            // Validation du token d'accès
            if (request.getAccessToken() == null || request.getAccessToken().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Token d'accès LinkedIn requis"));
            }

            // 1. Récupérer le profil LinkedIn via API
            Map<String, Object> linkedInData = linkedInService.getLinkedInProfile(request.getAccessToken());
            
            // 2. Créer utilisateur à partir des données LinkedIn
            User user = linkedInService.createUserFromLinkedIn(linkedInData);
            
            // 3. Générer email et mot de passe temporaires si pas fournis
            if (request.getEmail() != null) {
                user.setEmail(request.getEmail());
            } else {
                // Générer email basé sur LinkedIn ID
                user.setEmail("linkedin_" + user.getLinkedinToken() + "@easyapply.com");
            }
            
            // 4. Créer l'utilisateur via UserService
            User savedUser = userService.createUserFromLinkedIn(
                user.getEmail(),
                user.getLinkedinToken(),
                user.getFirstName(),
                user.getLastName()
            );
            
            // 5. Créer profil professionnel à partir des données LinkedIn
            Profile profile = linkedInService.createProfileFromLinkedIn(linkedInData, savedUser);
            savedUser.setProfile(profile);
            userService.updateUserProfile(savedUser.getId(), Map.of(
                "summary", profile.getSummary() != null ? profile.getSummary() : "",
                "skills", profile.getSkills() != null ? profile.getSkills() : "",
                "experiences", profile.getExperiences() != null ? profile.getExperiences() : "",
                "formations", profile.getFormations() != null ? profile.getFormations() : ""
            ));
            
            // 6. Générer token JWT
            String jwtToken = authService.generateJwtToken(
                savedUser.getId(),
                savedUser.getEmail(),
                "EasyApply",
               

               
                savedUser.getRole().toString()
            );
            
            // 7. Générer recommandations basées sur le profil
            Map<String, Object> recommendations = linkedInService.generateRecommendationsFromProfile(profile);
            
            //Réponse 8.  complète
            return ResponseEntity.ok(Map.of(
                "message", "Profil LinkedIn importé avec succès !",
                "token", "b",//Réponse 8
                "user", Map.of(
                    "id", savedUser.getId(),
                    "email", savedUser.getEmail(),
                    "firstName", savedUser.getFirstName(),
                    "lastName", savedUser.getLastName(),
                    "linkedinImported", true
                ),
                "profile", Map.of(
                    "summary", profile.getSummary() != null ? profile.getSummary() : "",
                    "skills", profile.getSkills() != null ? profile.getSkills() : "",
                    "experiences", profile.getExperiences() != null ? profile.getExperiences() : "",
                    "completeness", profile.getProfileCompleteness()
                ),
                "recommendations", recommendations
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de l'import LinkedIn: " + e.getMessage()));
        }
    }

    @PostMapping("/update-profile")
    @Operation(summary = "Mettre à jour depuis LinkedIn", 
               description = "Mettre à jour le profil existant avec nouvelles données LinkedIn")
    public ResponseEntity<?> updateFromLinkedIn(
            @RequestBody LinkedInUpdateRequest request,
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            // Validation du token JWT
            String token = authHeader.replace("Bearer ", "");
            if (!authService.validateJwtToken(token)) {
                return ResponseEntity.status(401)
                    .body(Map.of("error", "Token invalide"));
            }
            
            Long userId = authService.getUserIdFromToken(token);
            
            // Récupérer nouvelles données LinkedIn
            Map<String, Object> linkedInData = linkedInService.getLinkedInProfile(request.getAccessToken());
            
            // Mettre à jour le profil existant
            User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
            Profile updatedProfile = linkedInService.createProfileFromLinkedIn(linkedInData, user);
            
            // Sauvegarder les modifications
            userService.updateUserProfile(userId, Map.of(
                "summary", updatedProfile.getSummary() != null ? updatedProfile.getSummary() : "",
                "skills", updatedProfile.getSkills() != null ? updatedProfile.getSkills() : "",
                "experiences", updatedProfile.getExperiences() != null ? updatedProfile.getExperiences() : "",
                "formations", updatedProfile.getFormations() != null ? updatedProfile.getFormations() : ""
            ));
            
            // Nouvelles recommandations
            Map<String, Object> recommendations = linkedInService.generateRecommendationsFromProfile(updatedProfile);
            
            return ResponseEntity.ok(Map.of(
                "message", "Profil mis à jour depuis LinkedIn !",
                "profile", Map.of(
                    "summary", updatedProfile.getSummary(),
                    "skills", updatedProfile.getSkills(),
                    "completeness", updatedProfile.getProfileCompleteness()
                ),
                "recommendations", recommendations
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la mise à jour: " + e.getMessage()));
        }
    }

    // Classes DTO pour les requêtes
    public static class LinkedInImportRequest {
        private String accessToken;
        private String email; // Optionnel

        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class LinkedInUpdateRequest {
        private String accessToken;

        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    }
}