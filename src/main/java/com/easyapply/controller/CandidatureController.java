package com.easyapply.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.easyapply.entity.Application;
import com.easyapply.entity.Application.ApplicationStatus;
import com.easyapply.service.ApplicationService;
import com.easyapply.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/candidatures")
@CrossOrigin(origins = "*")
@Tag(name = "Candidatures", description = "API de gestion des candidatures")
public class CandidatureController {

    @Autowired
    private ApplicationService applicationService;
    
    @Autowired
    private AuthService authService;

    @PostMapping
    @Operation(summary = "Postuler à une offre", 
               description = "Créer une nouvelle candidature pour une offre d'emploi")
    public ResponseEntity<?> postuler(@RequestBody PostulerRequest request) {
        try {
            // Validation basique
            if (request.getUserId() == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID utilisateur requis"));
            }
            
            if (request.getJobId() == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID de l'offre requis"));
            }

            // ✅ CRÉER LA CANDIDATURE RÉELLEMENT
            Application application = applicationService.createApplication(
                request.getUserId(),
                request.getJobId(),
                request.getCoverLetter()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Candidature envoyée avec succès !");
            response.put("candidature", convertApplicationToMap(application));
            response.put("nextSteps", Arrays.asList(
                "Votre candidature sera examinée par l'entreprise",
                "Vous recevrez une notification en cas de réponse",
                "Vous pouvez suivre le statut dans 'Mes candidatures'"
            ));

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la postulation: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Mes candidatures", 
               description = "Récupérer toutes les candidatures d'un utilisateur")
    public ResponseEntity<?> getMesCandidatures(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long userId,
            @Parameter(description = "Statut des candidatures") @RequestParam(required = false) String status) {
        
        try {
            // ✅ RÉCUPÉRER LES VRAIES CANDIDATURES
            List<Application> applications;
            
            if (status != null && !status.isEmpty()) {
                try {
                    ApplicationStatus statusEnum = ApplicationStatus.valueOf(status.toUpperCase());
                    applications = applicationService.getApplicationsByUserAndStatus(userId, statusEnum);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest()
                        .body(Map.of("error", "Statut invalide: " + status));
                }
            } else {
                applications = applicationService.getApplicationsByUser(userId);
            }
            
            // ✅ CORRECTION - Convertir en Map pour la réponse avec type explicite
            List<Map<String, Object>> candidaturesList = applications.stream()
                .map(this::convertApplicationToDetailedMap)
                .collect(Collectors.toList());
            
            // ✅ CALCULER LES VRAIES STATISTIQUES
            Map<String, Object> stats = applicationService.getApplicationStatsForUser(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("candidatures", candidaturesList);
            response.put("total", candidaturesList.size());
            response.put("statistiques", stats.get("byStatus"));
            response.put("filtres", Map.of("status", status != null ? status : "Tous"));
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la récupération des candidatures: " + e.getMessage()));
        }
    }

    @GetMapping("/{candidatureId}")
    @Operation(summary = "Détail d'une candidature", 
               description = "Récupérer les détails complets d'une candidature")
    public ResponseEntity<?> getDetailCandidature(@PathVariable Long candidatureId) {
        try {
            // ✅ RÉCUPÉRER LA VRAIE CANDIDATURE
            Optional<Application> applicationOpt = applicationService.getApplicationById(candidatureId);
            
            if (applicationOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Application application = applicationOpt.get();
            
            // ✅ MARQUER COMME VUE SI NÉCESSAIRE
            if (application.getStatus() == ApplicationStatus.PENDING) {
                application = applicationService.viewApplication(candidatureId);
            }
            
            Map<String, Object> candidature = convertApplicationToDetailedMap(application);
            
            // Ajouter la timeline
            List<Map<String, Object>> timeline = new ArrayList<>();
            
            Map<String, Object> event1 = new HashMap<>();
            event1.put("date", application.getAppliedAt());
            event1.put("status", "PENDING");
            event1.put("description", "Candidature envoyée");
            timeline.add(event1);
            
            if (application.getStatus() != ApplicationStatus.PENDING) {
                Map<String, Object> event2 = new HashMap<>();
                event2.put("date", application.getUpdatedAt());
                event2.put("status", application.getStatus().toString());
                event2.put("description", getStatusDescription(application.getStatus()));
                timeline.add(event2);
            }
            
            candidature.put("timeline", timeline);
            
            return ResponseEntity.ok(candidature);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la récupération du détail: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{candidatureId}")
    @Operation(summary = "Retirer une candidature", 
               description = "Annuler/retirer une candidature (si encore possible)")
    public ResponseEntity<?> retirerCandidature(
            @PathVariable Long candidatureId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // ✅ RÉCUPÉRER L'UTILISATEUR DEPUIS LE TOKEN
            Long userId = null;
            if (authHeader != null) {
                String token = authHeader.replace("Bearer ", "");
                if (authService.validateJwtToken(token)) {
                    userId = authService.getUserIdFromToken(token);
                }
            }
            
            if (userId == null) {
                return ResponseEntity.status(401)
                    .body(Map.of("error", "Authentification requise"));
            }
            
            // ✅ RETIRER LA CANDIDATURE RÉELLEMENT
            applicationService.withdrawApplication(candidatureId, userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Candidature retirée avec succès");
            response.put("candidatureId", candidatureId);
            response.put("newStatus", "WITHDRAWN");
            
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors du retrait de la candidature: " + e.getMessage()));
        }
    }

    @GetMapping("/stats/user/{userId}")
    @Operation(summary = "Statistiques candidatures utilisateur", 
               description = "Obtenir des statistiques détaillées des candidatures d'un utilisateur")
    public ResponseEntity<?> getStatsCandidatures(@PathVariable Long userId) {
        try {
            // ✅ RÉCUPÉRER LES VRAIES STATISTIQUES
            Map<String, Object> stats = applicationService.getApplicationStatsForUser(userId);
            
            // Ajouter des statistiques supplémentaires
            List<Application> recentApplications = applicationService.getRecentApplicationsByUser(userId, 30);
            stats.put("candidaturesRecentes", recentApplications.size());
            
            // ✅ CORRECTION - Évolution mensuelle (simplifiée) avec type explicite
            List<Application> allApplications = applicationService.getApplicationsByUser(userId);
            Map<String, Long> monthlyStats = allApplications.stream()
                .collect(Collectors.groupingBy(
                    app -> app.getAppliedAt().getYear() + "-" + 
                           String.format("%02d", app.getAppliedAt().getMonthValue()),
                    Collectors.counting()
                ));
            
            List<Map<String, Object>> evolutionMensuelle = new ArrayList<>();
            for (Map.Entry<String, Long> entry : monthlyStats.entrySet()) {
                Map<String, Object> monthData = new HashMap<>();
                monthData.put("mois", entry.getKey());
                monthData.put("candidatures", entry.getValue());
                evolutionMensuelle.add(monthData);
            }
            
            // Trier par mois décroissant et limiter à 6
            evolutionMensuelle.sort((a, b) -> ((String) b.get("mois")).compareTo((String) a.get("mois")));
            if (evolutionMensuelle.size() > 6) {
                evolutionMensuelle = evolutionMensuelle.subList(0, 6);
            }
            
            stats.put("evolutionMensuelle", evolutionMensuelle);
            
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la récupération des statistiques: " + e.getMessage()));
        }
    }

    // ✅ MÉTHODES UTILITAIRES
    private Map<String, Object> convertApplicationToMap(Application application) {
        Map<String, Object> appMap = new HashMap<>();
        appMap.put("id", application.getId());
        appMap.put("userId", application.getUser().getId());
        appMap.put("jobId", application.getJob().getId());
        appMap.put("status", application.getStatus());
        appMap.put("appliedAt", application.getAppliedAt());
        appMap.put("coverLetter", application.getCoverLetter());
        return appMap;
    }
    
    private Map<String, Object> convertApplicationToDetailedMap(Application application) {
        Map<String, Object> appMap = convertApplicationToMap(application);
        
        // Ajouter les informations de l'offre
        appMap.put("jobTitle", application.getJob().getTitle());
        appMap.put("company", application.getJob().getCompany());
        appMap.put("jobLocation", application.getJob().getLocation());
        appMap.put("jobSalary", application.getJob().getSalary());
        appMap.put("contractType", application.getJob().getContractType());
        
        // Informations supplémentaires
        appMap.put("updatedAt", application.getUpdatedAt());
        
        return appMap;
    }
    
    // ✅ CORRECTION - Utiliser switch expression ou switch classique
    private String getStatusDescription(ApplicationStatus status) {
        return switch (status) {
            case PENDING -> "En attente d'examen";
            case VIEWED -> "Candidature consultée par l'entreprise";
            case ACCEPTED -> "Candidature acceptée";
            case REJECTED -> "Candidature refusée";
        };
    }

    // Classes DTO pour les requêtes
    public static class PostulerRequest {
        private Long userId;
        private Long jobId;
        private String coverLetter;

        // Getters et Setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        
        public Long getJobId() { return jobId; }
        public void setJobId(Long jobId) { this.jobId = jobId; }
        
        public String getCoverLetter() { return coverLetter; }
        public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }
    }
}