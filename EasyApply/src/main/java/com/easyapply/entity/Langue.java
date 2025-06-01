package com.easyapply.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "profil_langues")
public class Langue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String niveau;  // A1, A2, B1, B2, C1, C2
    
    @ManyToOne
    @JoinColumn(name = "profil_id")
    private Profil profil;
    
    // Getters and setters
}
