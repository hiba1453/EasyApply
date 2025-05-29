package com.easyapply.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private String location;
    
    @Column(nullable = false)
    private String company;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal salary;
    
    @Column(columnDefinition = "TEXT")
    private String requirements;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type")
    private ContractType contractType = ContractType.CDI;
    
    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.ACTIVE;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // ✅ NOUVELLE RELATION VERS ENTREPRISE
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;
    
    // ✅ NOUVELLE RELATION VERS APPLICATIONS
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Application> applications = new ArrayList<>();
    
    // ✅ NOUVELLE RELATION VERS RECOMMENDATIONS
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Recommendation> recommendations = new ArrayList<>();
    
    public enum ContractType {
        CDI, CDD, FREELANCE, STAGE
    }
    
    public enum JobStatus {
        ACTIVE, PAUSED, CLOSED
    }
    
    // Constructeurs
    public Job() {}
    
    public Job(String title, String description, String location, String company) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.company = company;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    
    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }
    
    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    
    public ContractType getContractType() { return contractType; }
    public void setContractType(ContractType contractType) { this.contractType = contractType; }
    
    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    // ✅ NOUVEAUX GETTERS/SETTERS POUR LES RELATIONS
    public Entreprise getEntreprise() { return entreprise; }
    public void setEntreprise(Entreprise entreprise) { this.entreprise = entreprise; }
    
    public List<Application> getApplications() { return applications; }
    public void setApplications(List<Application> applications) { this.applications = applications; }
    
    public List<Recommendation> getRecommendations() { return recommendations; }
    public void setRecommendations(List<Recommendation> recommendations) { this.recommendations = recommendations; }
    
    // ✅ MÉTHODES UTILITAIRES POUR GÉRER LES RELATIONS
    public void addApplication(Application application) {
        applications.add(application);
        application.setJob(this);
    }
    
    public void removeApplication(Application application) {
        applications.remove(application);
        application.setJob(null);
    }
    
    public void addRecommendation(Recommendation recommendation) {
        recommendations.add(recommendation);
        recommendation.setJob(this);
    }
    
    public void removeRecommendation(Recommendation recommendation) {
        recommendations.remove(recommendation);
        recommendation.setJob(null);
    }
}