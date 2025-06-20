package com.easyapply.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "profil_formations")
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String diplome;
    
    @Column(nullable = false)
    private String etablissement;
    
    @Column
    private LocalDate dateObtention;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "profil_id")
    private Profil profil;
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDiplome() {
        return diplome;
    }
    public void setDiplome(String diplome) {
        this.diplome = diplome;
    }
    public String getEtablissement() {
        return etablissement;
    }
    public void setEtablissement(String etablissement) {
        this.etablissement = etablissement;
    }
    public LocalDate getDateObtention() {
        return dateObtention;
    }
    public void setDateObtention(LocalDate dateObtention) {
        this.dateObtention = dateObtention;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Profil getProfil() {
        return profil;
    }
    public void setProfil(Profil profil) {
        this.profil = profil;
    }
    @Override
    public String toString() {
        return "Formation{" +
                "id=" + id +
                ", diplome='" + diplome + '\'' +
                ", etablissement='" + etablissement + '\'' +
                ", dateObtention=" + dateObtention +
                ", description='" + description + '\'' +
                ", profil=" + profil +
                '}';
    }
}
