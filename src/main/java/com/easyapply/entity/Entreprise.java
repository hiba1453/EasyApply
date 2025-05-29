package com.easyapply.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entreprises")
public class Entreprise {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String nom;
    
    @Column
    private String secteur;
    
    @Column(name = "site_web")
    private String siteWeb;
    
    @Column
    private String adresse;
    
    @Column(name = "numero_siret")
    private String numeroSiret;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relations JPA
    @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Job> jobs = new ArrayList<>();
    
    // Constructeurs
    public Entreprise() {}
    
    public Entreprise(String email, String password, String nom) {
        this.email = email;
        this.password = password;
        this.nom = nom;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getSecteur() { return secteur; }
    public void setSecteur(String secteur) { this.secteur = secteur; }
    
    public String getSiteWeb() { return siteWeb; }
    public void setSiteWeb(String siteWeb) { this.siteWeb = siteWeb; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public String getNumeroSiret() { return numeroSiret; }
    public void setNumeroSiret(String numeroSiret) { this.numeroSiret = numeroSiret; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    public List<Job> getJobs() { return jobs; }
    public void setJobs(List<Job> jobs) { this.jobs = jobs; }
    
    // Méthodes utilitaires
    public void addJob(Job job) {
        jobs.add(job);
        job.setEntreprise(this);
    }
    
    public void removeJob(Job job) {
        jobs.remove(job);
        job.setEntreprise(null);
    }
}