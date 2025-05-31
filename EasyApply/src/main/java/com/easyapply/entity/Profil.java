package com.easyapply.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "profil")
public class Profil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titreProfessionnel;
    private String competences;
    private String experiences;
    private String formations;
    private String langues;
    
    @OneToOne
    @JoinColumn(name = "candidat_id", nullable = false, unique = true)
    private Candidat candidat;
    
    // Getters, setters, constructeurs...
}
