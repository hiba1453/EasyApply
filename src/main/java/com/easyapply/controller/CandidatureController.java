package com.easyapply.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.easyapply.entity.Application;
import com.easyapply.entity.Application.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/candidatures")
@CrossOrigin(origins = "*")
@Tag(name = "Candidatures", description = "API de gestion des candidatures")
public class CandidatureController {

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

            // Vérifier si l'utilisateur a déjà postulé (simulation)
            // Dans une vraie implémentation, on vérifierait en base
            
            // Créer la candidature (simulation)
            Map<String, Object> nouvelleCandidature = new HashMap<>();
            nouvelleCandidature.put("id", System.currentTimeMillis());
            nouvelleCandidature.put("userId", request.getUserId());
            nouvelleCandidature.put("jobId", request.getJobId());
            nouvelleCandidature.put("coverLetter", request.getCoverLetter());
            nouvelleCandidature.put("status", ApplicationStatus.PENDING.toString());
            nouvelleCandidature.put("appliedAt", LocalDateTime.now());
            
            // Récupérer infos de l'offre (simulation)
            Map<String, Object> jobInfo = getJobInfo(request.getJobId());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Candidature envoyée avec succès !");
            response.put("candidature", nouvelleCandidature);
            response.put("offre", jobInfo);
            response.put("nextSteps", Arrays.asList(
                "Votre candidature sera examinée par l'entreprise",
                "Vous recevrez une notification en cas de réponse",
                "Vous pouvez suivre le statut dans 'Mes candidatures'"
            ));

            return ResponseEntity.ok(response);

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
            // Simulation des candidatures de l'utilisateur
            List<Map<String, Object>> candidatures = new ArrayList<>();
            
            // Candidature 1
            Map<String, Object> candidature1 = new HashMap<>();
            candidature1.put("id", 1L);
            candidature1.put("jobId", 1L);
            candidature1.put("jobTitle", "Développeur Full Stack Java/React");
            candidature1.put("company", "TechCorp");
            candidature1.put("status", ApplicationStatus.VIEWED.toString());
            candidature1.put("appliedAt", LocalDateTime.now().minusDays(5));
            candidature1.put("coverLetter", "Motivé par ce poste qui correspond parfaitement à mon profil...");
            candidature1.put("jobLocation", "Paris, France");
            candidature1.put("jobSalary", 45000);
            
            // Candidature 2
            Map<String, Object> candidature2 = new HashMap<>();
            candidature2.put("id", 2L);
            candidature2.put("jobId", 4L);
            candidature2.put("jobTitle", "Backend Developer Spring Boot");
            candidature2.put("company", "EasyApply");
            candidature2.put("status", ApplicationStatus.PENDING.toString());
            candidature2.put("appliedAt", LocalDateTime.now().minusDays(2));
            candidature2.put("coverLetter", "Passionné par votre plateforme, je souhaite contribuer...");
            candidature2.put("jobLocation", "Remote, France");
            candidature2.put("jobSalary", 48000);
            
            // Candidature 3
            Map<String, Object> candidature3 = new HashMap<>();
            candidature3.put("id", 3L);
            candidature3.put("jobId", 2L);
            candidature3.put("jobTitle", "Data Scientist Python");
            candidature3.put("company", "DataInnovation");
            candidature3.put("status", ApplicationStatus.REJECTED.toString());
            candidature3.put("appliedAt", LocalDateTime.now().minusDays(10));
            candidature3.put("coverLetter", "Expérience en analyse de données...");
            candidature3.put("jobLocation", "Lyon, France");
            candidature3.put("jobSalary", 52000);
            candidature3.put("rejectionReason", "Profil ne correspond pas exactement aux exigences");
            
            candidatures.add(candidature1);
            candidatures.add(candidature2);
            candidatures.add(candidature3);
            
            // Filtrage par statut si fourni
            if (status != null && !status.isEmpty()) {
                candidatures = candidatures.stream()
                    .filter(c -> c.get("status").toString().equalsIgnoreCase(status))
                    .toList();
            }
            
            // Statistiques
            Map<String, Long> statsStatuts = candidatures.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    c -> c.get("status").toString(),
                    java.util.stream.Collectors.counting()
                ));
            
            Map<String, Object> response = new HashMap<>();
            response.put("candidatures", candidatures);
            response.put("total", candidatures.size());
            response.put("statistiques", Map.of(
                "pending", statsStatuts.getOrDefault("PENDING", 0L),
                "viewed", statsStatuts.getOrDefault("VIEWED", 0L),
                "accepted", statsStatuts.getOrDefault("ACCEPTED", 0L),
                "rejected", statsStatuts.getOrDefault("REJECTED", 0L)
            ));
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
            // Simulation du détail d'une candidature
            Map<String, Object> candidature = new HashMap<>();
            candidature.put("id", candidatureId);
            candidature.put("status", ApplicationStatus.VIEWED.toString());
            candidature.put("appliedAt", LocalDateTime.now().minusDays(5));
            candidature.put("updatedAt", LocalDateTime.now().minusDays(2));
            candidature.put("coverLetter", "Motivé par ce poste qui correspond parfaitement à mon profil technique. J'ai 3 ans d'expérience en développement Full Stack avec Java/Spring Boot et React...");
            
            // Infos sur l'offre
            Map<String, Object> job = new HashMap<>();
            job.put("id", 1L);
            job.put("title", "Développeur Full Stack Java/React");
            job.put("company", "TechCorp");
            job.put("location", "Paris, France");
            job.put("salary", 45000);
            job.put("contractType", "CDI");
            job.put("description", "Rejoignez notre équipe pour développer des applications web modernes...");
            
            candidature.put("job", job);
            
            // Timeline de la candidature
            List<Map<String, Object>> timeline = new ArrayList<>();
            
            Map<String, Object> event1 = new HashMap<>();
            event1.put("date", LocalDateTime.now().minusDays(5));
            event1.put("status", "PENDING");
            event1.put("description", "Candidature envoyée");
            timeline.add(event1);
            
            Map<String, Object> event2 = new HashMap<>();
            event2.put("date", LocalDateTime.now().minusDays(2));
            event2.put("status", "VIEWED");
            event2.put("description", "Candidature consultée par l'entreprise");
            timeline.add(event2);
            
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
    public ResponseEntity<?> retirerCandidature(@PathVariable Long candidatureId) {
        try {
            // Vérifier si la candidature peut être retirée (simulation)
            // Généralement, on ne peut retirer que les candidatures PENDING
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Candidature retirée avec succès");
            response.put("candidatureId", candidatureId);
            response.put("newStatus", "WITHDRAWN");
            response.put("withdrawnAt", LocalDateTime.now());
            
            return ResponseEntity.ok(response);

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
            Map<String, Object> stats = new HashMap<>();
            
            // Statistiques générales
            Map<String, Object> general = new HashMap<>();
            general.put("totalCandidatures", 12);
            general.put("candidaturesEnAttente", 4);
            general.put("candidaturesVues", 5);
            general.put("candidaturesAcceptees", 2);
            general.put("candidaturesRefusees", 1);
            general.put("tauxReponse", 0.67); // (vues + acceptées + refusées) / total
            
            stats.put("general", general);
            
            // Statistiques par mois (3 derniers mois)
            List<Map<String, Object>> parMois = new ArrayList<>();
            
            Map<String, Object> mois1 = new HashMap<>();
            mois1.put("mois", "2024-11");
            mois1.put("candidatures", 5);
            mois1.put("reponses", 3);
            parMois.add(mois1);
            
            Map<String, Object> mois2 = new HashMap<>();
            mois2.put("mois", "2024-10");
            mois2.put("candidatures", 4);
            mois2.put("reponses", 2);
            parMois.add(mois2);
            
            Map<String, Object> mois3 = new HashMap<>();
            mois3.put("mois", "2024-09");
            mois3.put("candidatures", 3);
            mois3.put("reponses", 3);
            parMois.add(mois3);
            
            stats.put("evolutionMensuelle", parMois);
            
            // Top entreprises
            List<Map<String, Object>> topEntreprises = new ArrayList<>();
            topEntreprises.add(Map.of("entreprise", "TechCorp", "candidatures", 3));
            topEntreprises.add(Map.of("entreprise", "DataInnovation", "candidatures", 2));
            topEntreprises.add(Map.of("entreprise", "EasyApply", "candidatures", 2));
            
            stats.put("topEntreprises", topEntreprises);
            
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la récupération des statistiques: " + e.getMessage()));
        }
    }

    // Méthode utilitaire pour récupérer les infos d'une offre
    private Map<String, Object> getJobInfo(Long jobId) {
        Map<String, Object> job = new HashMap<>();
        
        switch (jobId.intValue()) {
            case 1:
                job.put("id", 1L);
                job.put("title", "Développeur Full Stack Java/React");
                job.put("company", "TechCorp");
                job.put("location", "Paris, France");
                job.put("salary", 45000);
                break;
            case 2:
                job.put("id", 2L);
                job.put("title", "Data Scientist Python");
                job.put("company", "DataInnovation");
                job.put("location", "Lyon, France");
                job.put("salary", 52000);
                break;
            default:
                job.put("id", jobId);
                job.put("title", "Offre d'emploi");
                job.put("company", "Entreprise");
                job.put("location", "France");
                job.put("salary", 40000);
        }
        
        return job;
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