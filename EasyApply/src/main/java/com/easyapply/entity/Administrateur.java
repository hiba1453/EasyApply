package com.easyapply.entity;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "administrateur")
public class Administrateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String motDePasse;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreationCompte = LocalDateTime.now();
    
    // Getters, setters, constructeurs...
}