package com.easyapply.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "Users", description = "API de gestion des utilisateurs et profils")
public class UserController {

    @GetMapping("/profile")
    @Operation(summary = "Profil utilisateur", 
               description = "Récupérer le profil de l'utilisateur connecté")
    public ResponseEntity<?> getProfile() {
        try {
            // Simulation du profil utilisateur connecté
            Map<String, Object> profile = new HashMap<>();
            profile.put("id", 1L);
            profile.put("email", "john.doe@easyapply.com");
            profile.put("firstName", "John");
            profile.put("lastName", "Doe");
            profile.put("phoneNumber", "+33 6 12 34 56 78");
            profile.put("role", "CANDIDATE");
            profile.put("isActive", true);
            profile.put("createdAt", LocalDateTime.now().minusDays(30));
            
            // Informations du profil professionnel
            Map<String, Object> professionalProfile = new HashMap<>();
            professionalProfile.put("summary", "Développeur Full Stack passionné par les technologies modernes");
            professionalProfile.put("skills", "Java, Spring Boot, React, PostgreSQL, Docker");
            professionalProfile.put("yearsOfExperience", 3);
            professionalProfile.put("preferredLocation", "Paris, France");
            professionalProfile.put("preferredSalary", 45000);
            
            profile.put("professionalProfile", professionalProfile);
            
            // Statistiques utilisateur
            Map<String, Object> stats = new HashMap<>();
            stats.put("cvUploaded", 2);
            stats.put("applications", 5);
            stats.put("recommendations", 12);
            stats.put("profileViews", 45);
            
            profile.put("stats", stats);
            
            return ResponseEntity.ok(profile);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la récupération du profil: " + e.getMessage()));
        }
    }

    @PutMapping("/profile")
    @Operation(summary = "Modifier le profil", 
               description = "Mettre à jour les informations du profil utilisateur")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request) {
        try {
            // Validation basique
            if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Prénom requis"));
            }
            
            if (request.getLastName() == null || request.getLastName().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Nom requis"));
            }
            
            // Simulation de mise à jour
            Map<String, Object> updatedProfile = new HashMap<>();
            updatedProfile.put("id", 1L);
            updatedProfile.put("email", "john.doe@easyapply.com");
            updatedProfile.put("firstName", request.getFirstName());
            updatedProfile.put("lastName", request.getLastName());
            updatedProfile.put("phoneNumber", request.getPhoneNumber());
            updatedProfile.put("summary", request.getSummary());
            updatedProfile.put("skills", request.getSkills());
            updatedProfile.put("yearsOfExperience", request.getYearsOfExperience());
            updatedProfile.put("preferredLocation", request.getPreferredLocation());
            updatedProfile.put("preferredSalary", request.getPreferredSalary());
            updatedProfile.put("updatedAt", LocalDateTime.now());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Profil mis à jour avec succès !");
            response.put("profile", updatedProfile);
            
            return ResponseEntity.ok(response);
            
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
            @RequestParam(defaultValue = "false") boolean isPrimary) {
        
        try {
            // Validation du fichier
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Fichier requis"));
            }
            
            if (!file.getContentType().equals("application/pdf")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Seuls les fichiers PDF sont autorisés"));
            }
            
            if (file.getSize() > 10 * 1024 * 1024) { // 10MB
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Fichier trop volumineux (max 10MB)"));
            }
            
            // Simulation d'upload
            Map<String, Object> cvInfo = new HashMap<>();
            cvInfo.put("id", System.currentTimeMillis());
            cvInfo.put("fileName", file.getOriginalFilename());
            cvInfo.put("fileSize", file.getSize());
            cvInfo.put("isPrimary", isPrimary);
            cvInfo.put("uploadedAt", LocalDateTime.now());
            cvInfo.put("status", "UPLOADED");
            cvInfo.put("analysisStatus", "PENDING");
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "CV uploadé avec succès !");
            response.put("cv", cvInfo);
            response.put("nextStep", "Votre CV sera analysé par notre IA pour extraire vos compétences");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de l'upload: " + e.getMessage()));
        }
    }

    @GetMapping("/cv")
    @Operation(summary = "Liste des CV", 
               description = "Récupérer la liste des CV uploadés par l'utilisateur")
    public ResponseEntity<?> getCVList() {
        try {
            // Simulation de liste de CV
            List<Map<String, Object>> cvList = new ArrayList<>();
            
            // CV 1
            Map<String, Object> cv1 = new HashMap<>();
            cv1.put("id", 1L);
            cv1.put("fileName", "CV_John_Doe_2024.pdf");
            cv1.put("fileSize", 256789);
            cv1.put("isPrimary", true);
            cv1.put("uploadedAt", LocalDateTime.now().minusDays(7));
            cv1.put("analysisStatus", "COMPLETED");
            cv1.put("extractedSkills", "Java, Spring Boot, React, PostgreSQL");
            cv1.put("extractionConfidence", 0.92);
            
            // CV 2
            Map<String, Object> cv2 = new HashMap<>();
            cv2.put("id", 2L);
            cv2.put("fileName", "CV_John_Doe_English.pdf");
            cv2.put("fileSize", 312456);
            cv2.put("isPrimary", false);
            cv2.put("uploadedAt", LocalDateTime.now().minusDays(15));
            cv2.put("analysisStatus", "COMPLETED");
            cv2.put("extractedSkills", "Full Stack Development, API Design, Agile");
            cv2.put("extractionConfidence", 0.88);
            
            cvList.add(cv1);
            cvList.add(cv2);
            
            Map<String, Object> response = new HashMap<>();
            response.put("cvList", cvList);
            response.put("total", cvList.size());
            response.put("primaryCV", cv1);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la récupération des CV: " + e.getMessage()));
        }
    }

    @GetMapping("/applications")
    @Operation(summary = "Mes candidatures", 
               description = "Récupérer la liste des candidatures de l'utilisateur")
    public ResponseEntity<?> getApplications() {
        try {
            // Simulation de candidatures
            List<Map<String, Object>> applications = new ArrayList<>();
            
            // Candidature 1
            Map<String, Object> app1 = new HashMap<>();
            app1.put("id", 1L);
            app1.put("jobId", 1L);
            app1.put("jobTitle", "Développeur Full Stack Java/React");
            app1.put("company", "TechCorp");
            app1.put("status", "VIEWED");
            app1.put("appliedAt", LocalDateTime.now().minusDays(5));
            app1.put("coverLetter", "Motivé par ce poste qui correspond parfaitement à mon profil...");
            
            // Candidature 2
            Map<String, Object> app2 = new HashMap<>();
            app2.put("id", 2L);
            app2.put("jobId", 4L);
            app2.put("jobTitle", "Backend Developer Spring Boot");
            app2.put("company", "EasyApply");
            app2.put("status", "PENDING");
            app2.put("appliedAt", LocalDateTime.now().minusDays(2));
            app2.put("coverLetter", "Passionné par votre plateforme, je souhaite contribuer...");
            
            applications.add(app1);
            applications.add(app2);
            
            Map<String, Object> response = new HashMap<>();
            response.put("applications", applications);
            response.put("total", applications.size());
            response.put("stats", Map.of(
                "pending", 1,
                "viewed", 1,
                "accepted", 0,
                "rejected", 0
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la récupération des candidatures: " + e.getMessage()));
        }
    }

    @GetMapping("/recommendations")
    @Operation(summary = "Recommandations d'emploi", 
               description = "Récupérer les offres recommandées par l'IA pour l'utilisateur")
    public ResponseEntity<?> getRecommendations() {
        try {
            // Simulation de recommandations IA
            List<Map<String, Object>> recommendations = new ArrayList<>();
            
            // Recommandation 1
            Map<String, Object> rec1 = new HashMap<>();
            rec1.put("id", 1L);
            rec1.put("jobId", 2L);
            rec1.put("jobTitle", "Data Scientist Python");
            rec1.put("company", "DataInnovation");
            rec1.put("matchScore", 0.87);
            rec1.put("reasoning", "Vos compétences en développement backend correspondent aux besoins en analyse de données");
            rec1.put("isViewed", false);
            rec1.put("createdAt", LocalDateTime.now().minusDays(1));
            
            // Recommandation 2
            Map<String, Object> rec2 = new HashMap<>();
            rec2.put("id", 2L);
            rec2.put("jobId", 1L);
            rec2.put("jobTitle", "Développeur Full Stack Java/React");
            rec2.put("company", "TechCorp");
            rec2.put("matchScore", 0.95);
            rec2.put("reasoning", "Correspondance parfaite avec votre stack technique Java/React");
            rec2.put("isViewed", true);
            rec2.put("createdAt", LocalDateTime.now().minusDays(3));
            
            recommendations.add(rec2); // Score le plus élevé en premier
            recommendations.add(rec1);
            
            Map<String, Object> response = new HashMap<>();
            response.put("recommendations", recommendations);
            response.put("total", recommendations.size());
            response.put("averageMatchScore", 0.91);
            response.put("newRecommendations", 1);
            
            return ResponseEntity.ok(response);
            
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