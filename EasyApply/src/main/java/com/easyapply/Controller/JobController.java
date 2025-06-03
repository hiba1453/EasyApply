package com.easyapply.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.easyapply.DTO.JobRequest;
import com.easyapply.Service.JobService;
import com.easyapply.entity.OffreEmploi;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.easyapply.config.CustomUserDetails;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Tag(name = "Job Management", description = "API pour la gestion des offres d'emploi")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    @Operation(summary = "Créer une nouvelle offre d'emploi", description = "Permet à une entreprise de créer une nouvelle offre d'emploi")
    public ResponseEntity<?> createJob(@RequestBody JobRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
                return ResponseEntity.status(401).body(Map.of("error", "Utilisateur non authentifié"));
            }

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getUserId();
            String role = userDetails.getRole();

            System.out.println("Authenticated userId: " + userId + ", role: " + role); // Debug log

            if (!"ENTREPRISE".equals(role)) {
                return ResponseEntity.status(403).body(Map.of("error", "Seules les entreprises peuvent créer des offres"));
            }
           if(!request.getEntrepriseId().equals(userId)) {
                return ResponseEntity.badRequest().body(Map.of("error", "vous ne pouvez pas créer une offre pour une autre entreprise"));
            }


            OffreEmploi job = jobService.createJob(request);
            return ResponseEntity.ok(Map.of(
                "message", "Offre créée avec succès",
                "job", job
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erreur lors de la création: " + e.getMessage()));
        }
    }

    @GetMapping("/company/{entrepriseId}")
    @Operation(summary = "Récupérer les offres d'une entreprise", description = "Récupère toutes les offres d'emploi d'une entreprise donnée")
    public ResponseEntity<?> getJobsByCompany(@PathVariable Long entrepriseId) {
        try {
            List<OffreEmploi> jobs = jobService.getJobsByCompany(entrepriseId);
            return ResponseEntity.ok(Map.of("jobs", jobs));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erreur lors de la récupération: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une offre par ID", description = "Récupère les détails d'une offre d'emploi")
    public ResponseEntity<?> getJobById(@PathVariable Long id) {
        try {
            Optional<OffreEmploi> job = jobService.getJobById(id);
            if (job.isPresent()) {
                return ResponseEntity.ok(Map.of("job", job.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erreur lors de la récupération: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une offre d'emploi", description = "Permet de modifier une offre d'emploi existante")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody JobRequest request) {
        try {
            Optional<OffreEmploi> updatedJob = jobService.updateJob(id, request);
            if (updatedJob.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "message", "Offre modifiée avec succès",
                    "job", updatedJob.get()
                ));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erreur lors de la modification: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une offre d'emploi", description = "Permet de supprimer une offre d'emploi")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        try {
            boolean deleted = jobService.deleteJob(id);
            if (deleted) {
                return ResponseEntity.ok(Map.of("message", "Offre supprimée avec succès"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erreur lors de la suppression: " + e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Récupérer toutes les offres", description = "Récupère toutes les offres d'emploi disponibles")
    public ResponseEntity<?> getAllJobs() {
        try {
            List<OffreEmploi> jobs = jobService.getAllJobs();
            return ResponseEntity.ok(Map.of("jobs", jobs));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erreur lors de la récupération: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des offres", description = "Recherche des offres par mots-clés")
    public ResponseEntity<?> searchJobs(@RequestParam String keywords) {
        try {
            List<OffreEmploi> jobs = jobService.searchJobs(keywords);
            return ResponseEntity.ok(Map.of("jobs", jobs));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erreur lors de la recherche: " + e.getMessage()));
        }
    }
}