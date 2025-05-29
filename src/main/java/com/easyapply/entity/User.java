package com.easyapply.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    // ✅ NOUVEAU CHAMP - Token LinkedIn pour OAuth
    @Column(name = "linkedin_token")
    private String linkedinToken;
    
    @Enumerated(EnumType.STRING)
    private Role role = Role.CANDIDATE;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // ✅ NOUVELLES RELATIONS JPA
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Profile profile;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CV> cvs = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Application> applications = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Recommendation> recommendations = new ArrayList<>();
    
    public enum Role {
        CANDIDATE, ADMIN
    }
    
    // Constructeurs
    public User() {}
    
    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Getters et Setters existants
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    // ✅ NOUVEAUX GETTERS/SETTERS
    public String getLinkedinToken() { return linkedinToken; }
    public void setLinkedinToken(String linkedinToken) { this.linkedinToken = linkedinToken; }
    
    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }
    
    public List<CV> getCvs() { return cvs; }
    public void setCvs(List<CV> cvs) { this.cvs = cvs; }
    
    public List<Application> getApplications() { return applications; }
    public void setApplications(List<Application> applications) { this.applications = applications; }
    
    public List<Recommendation> getRecommendations() { return recommendations; }
    public void setRecommendations(List<Recommendation> recommendations) { this.recommendations = recommendations; }
    
    // ✅ MÉTHODES UTILITAIRES POUR GÉRER LES RELATIONS
    public void addCV(CV cv) {
        cvs.add(cv);
        cv.setUser(this);
    }
    
    public void removeCV(CV cv) {
        cvs.remove(cv);
        cv.setUser(null);
    }
    
    public void addApplication(Application application) {
        applications.add(application);
        application.setUser(this);
    }
    
    public void removeApplication(Application application) {
        applications.remove(application);
        application.setUser(null);
    }
    
    public void addRecommendation(Recommendation recommendation) {
        recommendations.add(recommendation);
        recommendation.setUser(this);
    }
    
    public void removeRecommendation(Recommendation recommendation) {
        recommendations.remove(recommendation);
        recommendation.setUser(null);
    }
    
    // ✅ MÉTHODES MÉTIER (selon diagramme de classes)
    public void sInscrire() {
        // Logique d'inscription
        this.setIsActive(true);
        // createdAt est géré automatiquement par @CreationTimestamp
    }
    
    public boolean seConnecter(String email, String password) {
        // Logique de connexion - à implémenter avec AuthService
        return this.email.equals(email) && this.isActive;
    }
    
    public void postulerOffre(Job job, String coverLetter) {
        // Créer une nouvelle candidature
        Application application = new Application(this, job);
        application.setCoverLetter(coverLetter);
        this.addApplication(application);
    }
}