package com.easyapply.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "profil_experiences")
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String poste;
    
    @Column(nullable = false)
    private String entreprise;
    
    @Column
    private LocalDate dateDebut;
    
    @Column
    private LocalDate dateFin;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "profil_id")
    private Profil profil;
    
    // Getters and setters
}
