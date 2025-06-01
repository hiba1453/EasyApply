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
}
