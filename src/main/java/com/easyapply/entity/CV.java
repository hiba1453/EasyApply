package com.easyapply.entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "cv")
public class CV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nomFichier;
    
    
    @Column(nullable = false)
    private LocalDateTime dateUpload = LocalDateTime.now();
    
    @Column(nullable = false)
    private boolean estParDefaut = false;
    
    @ManyToOne
    @JoinColumn(name = "candidat_id", nullable = false)
    private Candidat candidat;
    
    @OneToMany(mappedBy = "cv")
    private List<Candidature> candidatures = new ArrayList<>();
    
    // Getters, setters, constructeurs...
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNomFichier() {
        return nomFichier;
    }
    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }
    
    public LocalDateTime getDateUpload() {
        return dateUpload;
    }
    public void setDateUpload(LocalDateTime dateUpload) {
        this.dateUpload = dateUpload;
    }
    public boolean isEstParDefaut() {
        return estParDefaut;
    }
    public void setEstParDefaut(boolean estParDefaut) {
        this.estParDefaut = estParDefaut;
    }
    public Candidat getCandidat() {
        return candidat;
    }
    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }
    public List<Candidature> getCandidatures() {
        return candidatures;
    }
    public void setCandidatures(List<Candidature> candidatures) {
        this.candidatures = candidatures;
    }
    public void addCandidature(Candidature candidature) {
        this.candidatures.add(candidature);
        candidature.setCv(this);
    }
    public void removeCandidature(Candidature candidature) {
        this.candidatures.remove(candidature);
        candidature.setCv(null);
    }
}