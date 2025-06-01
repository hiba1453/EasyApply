package com.easyapply.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.easyapply.DTO.ApplicationRequest;
import com.easyapply.DTO.ApplicationStatusRequest;
import com.easyapply.Service.ApplicationService;
import com.easyapply.entity.Candidature;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Tag(name = "Application Management", description = "API pour la gestion des candidatures")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    @Operation(summary = "Créer une nouvelle candidature", description = "Permet à un candidat de postuler à une offre")
    public ResponseEntity<?> createApplication(@RequestBody ApplicationRequest request) {
        try {
            // Validation
            if (request.getCandidatId() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "L'ID du candidat est requis"));
            }
            if (request.getOffreId() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "L'ID de l'offre est requis"));
            }

            Candidature application = applicationService.createApplication(request);
            return ResponseEntity.ok(Map.of(
                "message", "Candidature créée avec succès",
                "application", application
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erreur lors de la candidature: " + e.getMessage()));
        }
    }

    @GetMapping("/job/{jobId}")
    @Operation(summary = "Récupérer les candidatures d'une offre", description = "Récupère toutes les candidatures pour une offre donnée")
    public ResponseEntity<?> getApplicationsByJob(@PathVariable Long jobId) {
        try {
            List<Candidature> applications = applicationService.getApplicationsByJob(jobId);
            return ResponseEntity.ok(Map.of("applications", applications));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erreur lors de la récupération: " + e.getMessage()));
        }
    }

    @GetMapping("/candidate/{candidateId}")
    @Operation(summary = "Récupérer les candidatures d'un candidat", description = "Récupère toutes les candidatures d'un candidat")
    public ResponseEntity<?> getApplicationsByCandidate(@PathVariable Long candidateId) {
        try {
            List<Candidature> applications = applicationService.getApplicationsByCandidate(candidateId);
            return ResponseEntity.ok(Map.of("applications", applications));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erreur lors de la récupération: " + e.getMessage()));
        }
    }

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Récupérer les candidatures d'une entreprise", description = "Récupère toutes les candidatures reçues par une entreprise")
    public ResponseEntity<?> getApplicationsByCompany(@PathVariable Long companyId) {
        try {
            List<Candidature> applications = applicationService.getApplicationsByCompany(companyId);
            return ResponseEntity.ok(Map.of("applications", applications));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erreur lors de la récupération: " + e.getMessage()));
        }
    }

    @PutMapping("/{candidateId}/{jobId}/status")
    @Operation(summary = "Modifier le statut d'une candidature", description = "Permet à une entreprise de modifier le statut d'une candidature")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable Long candidateId, 
            @PathVariable Long jobId, 
            @RequestBody ApplicationStatusRequest request) {
        try {
            Candidature updatedApplication = applicationService.updateApplicationStatus(candidateId, jobId, request.getStatut());
            if (updatedApplication != null) {
                return ResponseEntity.ok(Map.of(
                    "message", "Statut mis à jour avec succès",
                    "application", updatedApplication
                ));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erreur lors de la mise à jour: " + e.getMessage()));
        }
    }

    @GetMapping("/{candidateId}/{jobId}")
    @Operation(summary = "Récupérer une candidature spécifique", description = "Récupère une candidature par candidat et offre")
    public ResponseEntity<?> getApplication(@PathVariable Long candidateId, @PathVariable Long jobId) {
        try {
            Candidature application = applicationService.getApplication(candidateId, jobId);
            if (application != null) {
                return ResponseEntity.ok(Map.of("application", application));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erreur lors de la récupération: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{candidateId}/{jobId}")
    @Operation(summary = "Supprimer une candidature", description = "Permet de supprimer une candidature")
    public ResponseEntity<?> deleteApplication(@PathVariable Long candidateId, @PathVariable Long jobId) {
        try {
            boolean deleted = applicationService.deleteApplication(candidateId, jobId);
            if (deleted) {
                return ResponseEntity.ok(Map.of("message", "Candidature supprimée avec succès"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erreur lors de la suppression: " + e.getMessage()));
        }
    }
}