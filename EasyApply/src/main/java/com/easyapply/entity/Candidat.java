package com.easyapply.entity;
import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "candidat")
public class Candidat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String motDePasse;
    
    private String linkedinToken;
    private String telephone;
    private LocalDate dateNaissance;
    
    @OneToOne(mappedBy = "candidat", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profil profil;
    
    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("estParDefaut DESC, dateUpload DESC")
    @JsonManagedReference
    private List<CV> cvs = new ArrayList<>();
    
    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Candidature> candidatures = new ArrayList<>();
    
    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommandation> recommandations = new ArrayList<>();
    
    // Getters, setters, constructeurs...
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
    public String getLinkedinToken() {
        return linkedinToken;
    }   
    public void setLinkedinToken(String linkedinToken) {
        this.linkedinToken = linkedinToken;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public LocalDate getDateNaissance() {
        return dateNaissance;
    }
    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
    public Profil getProfil() {
        return profil;
    }
    public void setProfil(Profil profil) {
        this.profil = profil;
    }
    public List<CV> getCvs() {
        return cvs;
    }
    public void setCvs(List<CV> cvs) {
        this.cvs = cvs;
    }
    public List<Candidature> getCandidatures() {
        return candidatures;
    }
    public void setCandidatures(List<Candidature> candidatures) {
        this.candidatures = candidatures;
    }
    public List<Recommandation> getRecommandations() {
        return recommandations;
    }
    public void setRecommandations(List
<Recommandation> recommandations) {
        this.recommandations = recommandations;
    }
    @Override
    public String toString() {
        return "Candidat{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                ", linkedinToken='" + linkedinToken + '\'' +
                ", telephone='" + telephone + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", profil=" + profil +
                ", cvs=" + cvs +
                ", candidatures=" + candidatures +
                ", recommandations=" + recommandations +
                '}';
    }
    
}