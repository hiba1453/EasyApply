package com.easyapply.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.easyapply.entity.Job;
import com.easyapply.entity.Job.ContractType;
import com.easyapply.service.AuthService;
import com.easyapply.service.JobService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
@Tag(name = "Jobs", description = "API de gestion des offres d'emploi")
public class JobController {

    @Autowired
    private JobService jobService;
    
    @Autowired
    private AuthService authService;

    @GetMapping
    @Operation(summary = "Liste des offres d'emploi", 
               description = "Récupérer toutes les offres d'emploi disponibles sur EasyApply")
    public ResponseEntity<?> getAllJobs(
            @Parameter(description = "Localisation pour filtrer les offres")
            @RequestParam(required = false) String location,
            @Parameter(description = "Type de contrat (CDI, CDD, FREELANCE, STAGE)")
            @RequestParam(required = false) String contractType,
            @Parameter(description = "Entreprise pour filtrer les offres")
            @RequestParam(required = false) String company,
            @Parameter(description = "Salaire minimum")
            @RequestParam(required = false) BigDecimal minSalary,
            @Parameter(description = "Page (défaut: 0)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille page (défaut: 20)")
            @RequestParam(defaultValue = "20") int size) {
        
        try {
            // ✅ UTILISER LE SERVICE RÉEL AVEC PAGINATION
            ContractType contractTypeEnum = null;
            if (contractType != null && !contractType.isEmpty()) {
                try {
                    contractTypeEnum = ContractType.valueOf(contractType.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest()
                        .body(Map.of("error", "Type de contrat invalide: " + contractType));
                }
            }

            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Job> jobsPage = jobService.searchJobs(location, contractTypeEnum, company, minSalary, pageRequest);
            
            // ✅ CONSTRUIRE LA RÉPONSE AVEC LES VRAIES DONNÉES
            List<Map<String, Object>> jobsList = jobsPage.getContent().stream()
                .map(this::convertJobToMap)
                .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("jobs", jobsList);
            response.put("total", jobsPage.getTotalElements());
            response.put("page", page);
            response.put("size", size);
            response.put("totalPages", jobsPage.getTotalPages());
            response.put("filters", Map.of(
                "location", location != null ? location : "Toutes",
                "contractType", contractType != null ? contractType : "Tous",
                "company", company != null ? company : "Toutes",
                "minSalary", minSalary != null ? minSalary : "Aucun minimum"
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la récupération des offres: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'une offre d'emploi", 
               description = "Récupérer les détails complets d'une offre d'emploi par son ID")
    public ResponseEntity<?> getJobById(
            @Parameter(description = "ID de l'offre d'emploi") 
            @PathVariable Long id) {
        
        try {
            // ✅ RÉCUPÉRER L'OFFRE RÉELLE DEPUIS LA BASE DE DONNÉES
            Optional<Job> jobOpt = jobService.getJobById(id);
            
            if (jobOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Job job = jobOpt.get();
            Map<String, Object> jobMap = convertJobToDetailedMap(job);
            
            return ResponseEntity.ok(jobMap);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la récupération de l'offre: " + e.getMessage()));
        }
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle offre d'emploi", 
               description = "Créer une nouvelle offre d'emploi (réservé aux entreprises)")
    public ResponseEntity<?> createJob(
            @RequestBody CreateJobRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Validation basique
            if (request.getTitle() == null || request.getTitle().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Titre de l'offre requis"));
            }
            
            if (request.getCompany() == null || request.getCompany().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Nom de l'entreprise requis"));
            }

            // ✅ RÉCUPÉRER L'ENTREPRISE DEPUIS LE TOKEN (SI FOURNI)
            Long entrepriseId = null;
            if (authHeader != null) {
                try {
                    String token = authHeader.replace("Bearer ", "");
                    if (authService.validateJwtToken(token)) {
                        String userType = authService.getUserTypeFromToken(token);
                        if ("ENTREPRISE".equals(userType)) {
                            entrepriseId = authService.getUserIdFromToken(token);
                        }
                    }
                } catch (Exception e) {
                    // Token invalide, continuer sans entreprise associée
                }
            }

            // ✅ CRÉER L'OFFRE RÉELLEMENT
            ContractType contractType = ContractType.CDI;
            if (request.getContractType() != null) {
                try {
                    contractType = ContractType.valueOf(request.getContractType().toUpperCase());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest()
                        .body(Map.of("error", "Type de contrat invalide"));
                }
            }

            Job newJob = jobService.createJob(
                entrepriseId,
                request.getTitle(),
                request.getDescription(),
                request.getLocation(),
                request.getCompany(),
                request.getSalary(),
                request.getRequirements(),
                contractType
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Offre d'emploi créée avec succès !");
            response.put("job", convertJobToDetailedMap(newJob));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la création de l'offre: " + e.getMessage()));
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "Statistiques des offres d'emploi", 
               description = "Obtenir des statistiques générales sur les offres d'emploi")
    public ResponseEntity<?> getJobStats() {
        try {
            // ✅ RÉCUPÉRER LES VRAIES STATISTIQUES
            Map<String, Object> stats = jobService.getJobStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la récupération des statistiques: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Recherche avancée d'offres", 
               description = "Rechercher des offres par mots-clés")
    public ResponseEntity<?> searchJobs(
            @Parameter(description = "Mots-clés de recherche")
            @RequestParam String keyword) {
        try {
            // ✅ RECHERCHE RÉELLE DANS LA BASE DE DONNÉES
            List<Job> jobs = jobService.searchJobsByKeyword(keyword);
            
            List<Map<String, Object>> jobsList = jobs.stream()
                .map(this::convertJobToMap)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(Map.of(
                "jobs", jobsList,
                "total", jobsList.size(),
                "keyword", keyword
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la recherche: " + e.getMessage()));
        }
    }

    // ✅ MÉTHODES UTILITAIRES POUR CONVERTIR LES ENTITÉS
    private Map<String, Object> convertJobToMap(Job job) {
        Map<String, Object> jobMap = new HashMap<>();
        jobMap.put("id", job.getId());
        jobMap.put("title", job.getTitle());
        jobMap.put("company", job.getCompany());
        jobMap.put("location", job.getLocation());
        jobMap.put("salary", job.getSalary());
        jobMap.put("contractType", job.getContractType());
        jobMap.put("status", job.getStatus());
        jobMap.put("createdAt", job.getCreatedAt());
        jobMap.put("description", job.getDescription().length() > 200 ? 
            job.getDescription().substring(0, 200) + "..." : job.getDescription());
        return jobMap;
    }
    
    private Map<String, Object> convertJobToDetailedMap(Job job) {
        Map<String, Object> jobMap = new HashMap<>();
        jobMap.put("id", job.getId());
        jobMap.put("title", job.getTitle());
        jobMap.put("company", job.getCompany());
        jobMap.put("location", job.getLocation());
        jobMap.put("salary", job.getSalary());
        jobMap.put("contractType", job.getContractType());
        jobMap.put("status", job.getStatus());
        jobMap.put("description", job.getDescription());
        jobMap.put("requirements", job.getRequirements());
        jobMap.put("createdAt", job.getCreatedAt());
        jobMap.put("updatedAt", job.getUpdatedAt());
        
        // Informations supplémentaires
        jobMap.put("applicationCount", job.getApplications().size());
        jobMap.put("canApply", job.getStatus() == Job.JobStatus.ACTIVE);
        
        // Informations entreprise si disponible
        if (job.getEntreprise() != null) {
            jobMap.put("entreprise", Map.of(
                "id", job.getEntreprise().getId(),
                "nom", job.getEntreprise().getNom(),
                "secteur", job.getEntreprise().getSecteur() != null ? job.getEntreprise().getSecteur() : ""
            ));
        }
        
        return jobMap;
    }

    // Classe DTO pour créer une offre
    public static class CreateJobRequest {
        private String title;
        private String company;
        private String location;
        private String description;
        private String requirements;
        private BigDecimal salary;
        private String contractType;

        // Getters et Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }
        
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getRequirements() { return requirements; }
        public void setRequirements(String requirements) { this.requirements = requirements; }
        
        public BigDecimal getSalary() { return salary; }
        public void setSalary(BigDecimal salary) { this.salary = salary; }
        
        public String getContractType() { return contractType; }
        public void setContractType(String contractType) { this.contractType = contractType; }
    }
}