package com.easyapply.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.easyapply.entity.Job;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
@Tag(name = "Jobs", description = "API de gestion des offres d'emploi")
public class JobController {

    @GetMapping
    @Operation(summary = "Liste des offres d'emploi", 
               description = "Récupérer toutes les offres d'emploi disponibles sur EasyApply")
    public ResponseEntity<?> getAllJobs(
            @Parameter(description = "Localisation pour filtrer les offres")
            @RequestParam(required = false) String location,
            @Parameter(description = "Type de contrat (CDI, CDD, FREELANCE, STAGE)")
            @RequestParam(required = false) String contractType,
            @Parameter(description = "Entreprise pour filtrer les offres")
            @RequestParam(required = false) String company) {
        
        try {
            // Simulation de données d'offres d'emploi
            List<Map<String, Object>> jobs = new ArrayList<>();
            
            // Offre 1
            Map<String, Object> job1 = new HashMap<>();
            job1.put("id", 1L);
            job1.put("title", "Développeur Full Stack Java/React");
            job1.put("company", "TechCorp");
            job1.put("location", "Paris, France");
            job1.put("salary", 45000);
            job1.put("contractType", "CDI");
            job1.put("status", "ACTIVE");
            job1.put("description", "Rejoignez notre équipe pour développer des applications web modernes avec Java Spring Boot et React.");
            job1.put("requirements", "3+ ans d'expérience en Java, Spring Boot, React, PostgreSQL");
            job1.put("createdAt", LocalDateTime.now().minusDays(2));
            
            // Offre 2
            Map<String, Object> job2 = new HashMap<>();
            job2.put("id", 2L);
            job2.put("title", "Data Scientist Python");
            job2.put("company", "DataInnovation");
            job2.put("location", "Lyon, France");
            job2.put("salary", 52000);
            job2.put("contractType", "CDI");
            job2.put("status", "ACTIVE");
            job2.put("description", "Analyser des données massives et développer des modèles d'IA pour nos clients.");
            job2.put("requirements", "Python, Machine Learning, TensorFlow, SQL, 2+ ans d'expérience");
            job2.put("createdAt", LocalDateTime.now().minusDays(5));
            
            // Offre 3
            Map<String, Object> job3 = new HashMap<>();
            job3.put("id", 3L);
            job3.put("title", "Stage Développement Mobile");
            job3.put("company", "MobileStart");
            job3.put("location", "Marseille, France");
            job3.put("salary", 1200);
            job3.put("contractType", "STAGE");
            job3.put("status", "ACTIVE");
            job3.put("description", "Développer des applications mobiles avec React Native et Flutter.");
            job3.put("requirements", "Étudiant en informatique, React Native ou Flutter, motivation");
            job3.put("createdAt", LocalDateTime.now().minusDays(1));
            
            // Offre 4 - EasyApply meta 😊
            Map<String, Object> job4 = new HashMap<>();
            job4.put("id", 4L);
            job4.put("title", "Backend Developer Spring Boot");
            job4.put("company", "EasyApply");
            job4.put("location", "Remote, France");
            job4.put("salary", 48000);
            job4.put("contractType", "CDI");
            job4.put("status", "ACTIVE");
            job4.put("description", "Développer l'API backend de la plateforme EasyApply avec Spring Boot et PostgreSQL.");
            job4.put("requirements", "Spring Boot, PostgreSQL, API REST, JWT, expérience 2+ ans");
            job4.put("createdAt", LocalDateTime.now());
            
            jobs.add(job1);
            jobs.add(job2);
            jobs.add(job3);
            jobs.add(job4);
            
            // Filtrage basique (simulation)
            if (location != null && !location.isEmpty()) {
                jobs = jobs.stream()
                    .filter(job -> job.get("location").toString().toLowerCase().contains(location.toLowerCase()))
                    .toList();
            }
            
            if (contractType != null && !contractType.isEmpty()) {
                jobs = jobs.stream()
                    .filter(job -> job.get("contractType").toString().equalsIgnoreCase(contractType))
                    .toList();
            }
            
            if (company != null && !company.isEmpty()) {
                jobs = jobs.stream()
                    .filter(job -> job.get("company").toString().toLowerCase().contains(company.toLowerCase()))
                    .toList();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("jobs", jobs);
            response.put("total", jobs.size());
            response.put("filters", Map.of(
                "location", location != null ? location : "Toutes",
                "contractType", contractType != null ? contractType : "Tous",
                "company", company != null ? company : "Toutes"
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
            // Simulation de récupération d'une offre par ID
            Map<String, Object> job = new HashMap<>();
            
            switch (id.intValue()) {
                case 1:
                    job.put("id", 1L);
                    job.put("title", "Développeur Full Stack Java/React");
                    job.put("company", "TechCorp");
                    job.put("location", "Paris, France");
                    job.put("salary", 45000);
                    job.put("contractType", "CDI");
                    job.put("status", "ACTIVE");
                    job.put("description", "Rejoignez notre équipe pour développer des applications web modernes avec Java Spring Boot et React. Vous travaillerez sur des projets innovants dans un environnement agile.");
                    job.put("requirements", "3+ ans d'expérience en Java, Spring Boot, React, PostgreSQL. Connaissance de Docker et AWS appréciée.");
                    job.put("benefits", "Télétravail partiel, mutuelle, tickets restaurant, formation continue");
                    job.put("applicationDeadline", LocalDateTime.now().plusDays(30));
                    job.put("viewCount", 156);
                    job.put("applicationCount", 23);
                    break;
                    
                case 2:
                    job.put("id", 2L);
                    job.put("title", "Data Scientist Python");
                    job.put("company", "DataInnovation");
                    job.put("location", "Lyon, France");
                    job.put("salary", 52000);
                    job.put("contractType", "CDI");
                    job.put("status", "ACTIVE");
                    job.put("description", "Analyser des données massives et développer des modèles d'IA pour nos clients dans divers secteurs.");
                    job.put("requirements", "Python, Machine Learning, TensorFlow, SQL, 2+ ans d'expérience en data science");
                    job.put("benefits", "Stock options, bureau moderne, équipe jeune et dynamique");
                    job.put("applicationDeadline", LocalDateTime.now().plusDays(20));
                    job.put("viewCount", 89);
                    job.put("applicationCount", 12);
                    break;
                    
                default:
                    return ResponseEntity.notFound().build();
            }
            
            return ResponseEntity.ok(job);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la récupération de l'offre: " + e.getMessage()));
        }
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle offre d'emploi", 
               description = "Créer une nouvelle offre d'emploi (réservé aux entreprises)")
    public ResponseEntity<?> createJob(@RequestBody CreateJobRequest request) {
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
            
            // Simulation de création d'offre
            Map<String, Object> newJob = new HashMap<>();
            newJob.put("id", System.currentTimeMillis()); // ID généré
            newJob.put("title", request.getTitle());
            newJob.put("company", request.getCompany());
            newJob.put("location", request.getLocation());
            newJob.put("description", request.getDescription());
            newJob.put("requirements", request.getRequirements());
            newJob.put("salary", request.getSalary());
            newJob.put("contractType", request.getContractType() != null ? request.getContractType() : "CDI");
            newJob.put("status", "ACTIVE");
            newJob.put("createdAt", LocalDateTime.now());
            newJob.put("viewCount", 0);
            newJob.put("applicationCount", 0);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Offre d'emploi créée avec succès !");
            response.put("job", newJob);
            
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
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalJobs", 4);
        stats.put("activeJobs", 4);
        stats.put("totalViews", 245);
        stats.put("totalApplications", 35);
        stats.put("topLocations", Arrays.asList("Paris", "Lyon", "Marseille"));
        stats.put("topCompanies", Arrays.asList("TechCorp", "DataInnovation", "EasyApply"));
        stats.put("contractTypes", Map.of(
            "CDI", 3,
            "CDD", 0,
            "FREELANCE", 0,
            "STAGE", 1
        ));
        
        return ResponseEntity.ok(stats);
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