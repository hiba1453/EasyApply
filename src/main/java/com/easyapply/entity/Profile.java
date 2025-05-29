package com.easyapply.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "profiles")
public class Profile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // ✅ REMPLACE userId par relation OneToOne
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // ✅ CHAMPS EXISTANTS
    @Column(columnDefinition = "TEXT")
    private String summary;
    
    @Column(columnDefinition = "TEXT")
    private String skills;
    
    // ✅ NOUVEAUX CHAMPS selon diagramme de classes
    @Column(columnDefinition = "TEXT")
    private String formations;
    
    @Column(columnDefinition = "TEXT")
    private String experiences;
    
    @Column(columnDefinition = "TEXT")
    private String hobbies;
    
    @Column(columnDefinition = "TEXT")
    private String benevolat;
    
    // ✅ CHAMPS PROFESSIONNELS supplémentaires
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;
    
    @Column(name = "preferred_location")
    private String preferredLocation;
    
    @Column(name = "preferred_salary")
    private Double preferredSalary;
    
    @Column(name = "availability")
    private String availability;
    
    @Column(name = "linkedin_url")
    private String linkedinUrl;
    
    @Column(name = "portfolio_url")
    private String portfolioUrl;
    
    // ✅ CHAMPS POUR L'IA
    @Column(name = "profile_completeness")
    private Double profileCompleteness = 0.0;
    
    @Column(name = "last_analyzed_at")
    private LocalDateTime lastAnalyzedAt;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructeurs
    public Profile() {}
    
    public Profile(User user) {
        this.user = user;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    // ✅ NOUVEAUX GETTERS/SETTERS POUR LA RELATION
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    // Getters/Setters existants
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    
    // ✅ NOUVEAUX GETTERS/SETTERS
    public String getFormations() { return formations; }
    public void setFormations(String formations) { this.formations = formations; }
    
    public String getExperiences() { return experiences; }
    public void setExperiences(String experiences) { this.experiences = experiences; }
    
    public String getHobbies() { return hobbies; }
    public void setHobbies(String hobbies) { this.hobbies = hobbies; }
    
    public String getBenevolat() { return benevolat; }
    public void setBenevolat(String benevolat) { this.benevolat = benevolat; }
    
    public Integer getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }
    
    public String getPreferredLocation() { return preferredLocation; }
    public void setPreferredLocation(String preferredLocation) { this.preferredLocation = preferredLocation; }
    
    public Double getPreferredSalary() { return preferredSalary; }
    public void setPreferredSalary(Double preferredSalary) { this.preferredSalary = preferredSalary; }
    
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    
    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }
    
    public String getPortfolioUrl() { return portfolioUrl; }
    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }
    
    public Double getProfileCompleteness() { return profileCompleteness; }
    public void setProfileCompleteness(Double profileCompleteness) { this.profileCompleteness = profileCompleteness; }
    
    public LocalDateTime getLastAnalyzedAt() { return lastAnalyzedAt; }
    public void setLastAnalyzedAt(LocalDateTime lastAnalyzedAt) { this.lastAnalyzedAt = lastAnalyzedAt; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    // ✅ MÉTHODE DE COMPATIBILITÉ (pour ne pas casser le code existant)
    public Long getUserId() { 
        return user != null ? user.getId() : null; 
    }
    
    // ✅ MÉTHODES MÉTIER (selon diagramme de classes)
    public void analyserProfil() {
        // Cette méthode sera appelée par le MoteurRecommandation
        this.setLastAnalyzedAt(LocalDateTime.now());
        
        // Calculer le pourcentage de complétude du profil
        double completeness = calculateProfileCompleteness();
        this.setProfileCompleteness(completeness);
    }
    
    private double calculateProfileCompleteness() {
        int totalFields = 8; // Nombre de champs importants
        int filledFields = 0;
        
        if (summary != null && !summary.isEmpty()) filledFields++;
        if (skills != null && !skills.isEmpty()) filledFields++;
        if (formations != null && !formations.isEmpty()) filledFields++;
        if (experiences != null && !experiences.isEmpty()) filledFields++;
        if (yearsOfExperience != null) filledFields++;
        if (preferredLocation != null && !preferredLocation.isEmpty()) filledFields++;
        if (preferredSalary != null) filledFields++;
        if (linkedinUrl != null && !linkedinUrl.isEmpty()) filledFields++;
        
        return (double) filledFields / totalFields;
    }
    
    public boolean isProfileComplete() {
        return profileCompleteness != null && profileCompleteness >= 0.8; // 80% de complétude
    }
}