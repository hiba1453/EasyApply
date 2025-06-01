package com.easyapply.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "profil_competences")
public class Competence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column
    private String niveau;
    
    @ManyToOne
    @JoinColumn(name = "profil_id")
    private Profil profil;
    
    // Getters and setters
}
