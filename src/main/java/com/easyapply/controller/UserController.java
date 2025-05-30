package com.easyapply.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.easyapply.entity.User;
import com.easyapply.service.ApplicationService;
import com.easyapply.service.AuthService;
import com.easyapply.service.CVService;
import com.easyapply.service.RecommendationService;
import com.easyapply.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "Users", description = "API de gestion des utilisateurs et profils")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private CVService cvService;
    
    @Autowired
    private ApplicationService applicationService;
    
    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/getprofile")
    @Operation(summary = "Profil utilisateur", 
               description = "Récupérer le profil de l'utilisateur connecté")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            // ✅ RÉCUPÉRER L'UTILISATEUR RÉEL DEPUIS LE TOKEN
            String token = authHeader.replace("Bearer ", "");
            if (!authService.validateJwtToken(token)) {
                return ResponseEntity.status(401)
                    .body(Map.of("error", "Token invalide"));
            }
            
            Long userId = authService.getUserIdFromToken(token);
            User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
            // ✅ CONSTRUIRE LA RÉPONSE AVEC LES VRAIES DONNÉES
            return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "phoneNumber", user.getPhoneNumber() != null ? user.getPhoneNumber() : "",
                "role", user.getRole(),
                "isActive", user.getIsActive(),
                "createdAt", user.getCreatedAt(),
                "professionalProfile", user.getProfile() != null ? Map.of(
                    "summary", user.getProfile().getSummary() != null ? user.getProfile().getSummary() : "",
                    "skills", user.getProfile().getSkills() != null ? user.getProfile().getSkills() : "",
                    "yearsOfExperience", user.getProfile().getYearsOfExperience() != null ? user.getProfile().getYearsOfExperience() : 0,
                    "preferredLocation", user.getProfile().getPreferredLocation() != null ? user.getProfile().getPreferredLocation() : "",
                    "preferredSalary", user.getProfile().getPreferredSalary() != null ? user.getProfile().getPreferredSalary() : 0,
                    "profileCompleteness", user.getProfile().getProfileCompleteness()
                ) : Map.of(),
                "stats", userService.getUserStats(userId)
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la récupération du profil: " + e.getMessage()));
        }
    }

    @PutMapping("/profile")
    @Operation(summary = "Modifier le profil", 
               description = "Mettre à jour les informations du profil utilisateur")
    public ResponseEntity<?> updateProfile(
            @RequestBody UpdateProfileRequest request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // ✅ RÉCUPÉRER L'UTILISATEUR RÉEL
            String token = authHeader.replace("Bearer ", "");
            Long userId = authService.getUserIdFromToken(token);
            
            // ✅ METTRE À JOUR AVEC LES VRAIES DONNÉES
            User updatedUser = userService.updateUserProfile(userId, Map.of(
                "firstName", request.getFirstName() != null ? request.getFirstName() : "",
                "lastName", request.getLastName() != null ? request.getLastName() : "",
                "phoneNumber", request.getPhoneNumber() != null ? request.getPhoneNumber() : "",
                "summary", request.getSummary() != null ? request.getSummary() : "",
                "skills", request.getSkills() != null ? request.getSkills() : "",
                "yearsOfExperience", request.getYearsOfExperience() != null ? request.getYearsOfExperience() : 0,
                "preferredLocation", request.getPreferredLocation() != null ? request.getPreferredLocation() : "",
                "preferredSalary", request.getPreferredSalary() != null ? request.getPreferredSalary() : 0.0
            ));
            
            return ResponseEntity.ok(Map.of(
                "message", "Profil mis à jour avec succès !",
                "profile", Map.of(
                    "id", updatedUser.getId(),
                    "email", updatedUser.getEmail(),
                    "firstName", updatedUser.getFirstName(),
                    "lastName", updatedUser.getLastName(),
                    "phoneNumber", updatedUser.getPhoneNumber() != null ? updatedUser.getPhoneNumber() : "",
                    "professionalProfile", updatedUser.getProfile() != null ? Map.of(
                        "summary", updatedUser.getProfile().getSummary() != null ? updatedUser.getProfile().getSummary() : "",
                        "skills", updatedUser.getProfile().getSkills() != null ? updatedUser.getProfile().getSkills() : "",
                        "yearsOfExperience", updatedUser.getProfile().getYearsOfExperience() != null ? updatedUser.getProfile().getYearsOfExperience() : 0,
                        "preferredLocation", updatedUser.getProfile().getPreferredLocation() != null ? updatedUser.getProfile().getPreferredLocation() : "",
                        "preferredSalary", updatedUser.getProfile().getPreferredSalary() != null ? updatedUser.getProfile().getPreferredSalary() : 0.0,
                        "profileCompleteness", updatedUser.getProfile().getProfileCompleteness() != null ? updatedUser.getProfile().getProfileCompleteness() : 0.0
                    ) : Map.of(),
                    "updatedAt", updatedUser.getUpdatedAt()
                )
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la mise à jour: " + e.getMessage()));
        }
    }

    @PostMapping("/upload-cv")
    @Operation(summary = "Upload de CV", 
               description = "Télécharger un fichier CV (PDF uniquement)")
    public ResponseEntity<?> uploadCV(
            @Parameter(description = "Fichier CV (PDF)")
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "Définir comme CV principal")
            @RequestParam(defaultValue = "false") boolean isPrimary,
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            // Validation du fichier
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Fichier requis"));
            }
            
            String contentType = file.getContentType();
            if (contentType == null || !contentType.equals("application/pdf")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Seuls les fichiers PDF sont autorisés"));
            }
            
            if (file.getSize() > 10 * 1024 * 1024) { // 10MB
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Fichier trop volumineux (max 10MB)"));
            }
            
            // ✅ RÉCUPÉRER L'UTILISATEUR RÉEL
            String token = authHeader.replace("Bearer ", "");
            Long userId = authService.getUserIdFromToken(token);
            
            // ✅ SAUVEGARDER LE CV RÉELLEMENT
            var cv = cvService.saveCV(userId, file, isPrimary);
            
            return ResponseEntity.ok(Map.of(
                "message", "CV uploadé avec succès !",
                "cv", Map.of(
                    "id", cv.getId(),
                    "fileName", cv.getFileName(),
                    "fileSize", cv.getFileSize(),
                    "isPrimary", cv.getIsPrimary(),
                    "uploadedAt", cv.getUploadedAt(),
                    "analysisStatus", cv.getAnalysisStatus()
                ),
                "nextStep", "Votre CV sera analysé par notre IA pour extraire vos compétences"
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de l'upload: " + e.getMessage()));
        }
    }

    @GetMapping("/cv")
    @Operation(summary = "Liste des CV", 
               description = "Récupérer la liste des CV uploadés par l'utilisateur")
    public ResponseEntity<?> getCVList(@RequestHeader("Authorization") String authHeader) {
        try {
            // ✅ RÉCUPÉRER LES VRAIS CV DE L'UTILISATEUR
            String token = authHeader.replace("Bearer ", "");
            Long userId = authService.getUserIdFromToken(token);
            
            List<Map<String, Object>> cvList = cvService.getUserCVs(userId);
            var primaryCV = cvService.getPrimaryCV(userId);
            
            return ResponseEntity.ok(Map.of(
                "cvList", cvList,
                "total", cvList.size(),
                "primaryCV", primaryCV.orElse(null)
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la récupération des CV: " + e.getMessage()));
        }
    }

    @GetMapping("/applications")
    @Operation(summary = "Mes candidatures", 
               description = "Récupérer la liste des candidatures de l'utilisateur")
    public ResponseEntity<?> getApplications(@RequestHeader("Authorization") String authHeader) {
        try {
            // ✅ RÉCUPÉRER LES VRAIES CANDIDATURES
            String token = authHeader.replace("Bearer ", "");
            Long userId = authService.getUserIdFromToken(token);
            
            var applications = applicationService.getApplicationsByUser(userId);
            var stats = applicationService.getApplicationStatsForUser(userId);
            
            return ResponseEntity.ok(Map.of(
                "applications", applications,
                "total", applications.size(),
                "stats", stats
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la récupération des candidatures: " + e.getMessage()));
        }
    }

    @GetMapping("/recommendations")
    @Operation(summary = "Recommandations d'emploi", 
               description = "Récupérer les offres recommandées par l'IA pour l'utilisateur")
    public ResponseEntity<?> getRecommendations(@RequestHeader("Authorization") String authHeader) {
        try {
            // ✅ RÉCUPÉRER LES VRAIES RECOMMANDATIONS
            String token = authHeader.replace("Bearer ", "");
            Long userId = authService.getUserIdFromToken(token);
            
            List<Map<String, Object>> recommendations = recommendationService.getRecommendationsForUser(userId);
            
            // ✅ Calculer les statistiques à partir de la liste
            double averageMatchScore = recommendations.isEmpty() ? 0.0 : 
                recommendations.stream()
                    .mapToDouble(r -> (Double) r.get("matchScore"))
                    .average().orElse(0.0);
            
            long newRecommendations = recommendations.stream()
                .mapToLong(r -> (Boolean) r.get("isViewed") ? 0 : 1)
                .sum();
            
            return ResponseEntity.ok(Map.of(
                "recommendations", recommendations,
                "total", recommendations.size(),
                "averageMatchScore", averageMatchScore,
                "newRecommendations", newRecommendations
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la récupération des recommandations: " + e.getMessage()));
        }
    }

    // Classe DTO pour mise à jour du profil
    public static class UpdateProfileRequest {
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String summary;
        private String skills;
        private Integer yearsOfExperience;
        private String preferredLocation;
        private Double preferredSalary;

        // Getters et Setters
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
        
        public String getSkills() { return skills; }
        public void setSkills(String skills) { this.skills = skills; }
        
        public Integer getYearsOfExperience() { return yearsOfExperience; }
        public void setYearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }
        
        public String getPreferredLocation() { return preferredLocation; }
        public void setPreferredLocation(String preferredLocation) { this.preferredLocation = preferredLocation; }
        
        public Double getPreferredSalary() { return preferredSalary; }
        public void setPreferredSalary(Double preferredSalary) { this.preferredSalary = preferredSalary; }
    }
}