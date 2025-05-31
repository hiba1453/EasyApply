package com.easyapply.entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.*;

import jakarta.persistence.*;


@Entity
@Table(name = "offre_emploi")
public class OffreEmploi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titre;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    private String motsCles;
    
    @Column(nullable = false)
    private LocalDateTime datePublication = LocalDateTime.now();
    
    private LocalDateTime dateExpiration;
    private String lieu;
    private String salaire;
    private String typeContrat;
    private String niveauExperience;
    
    @ManyToOne
    @JoinColumn(name = "entreprise_id", nullable = false)
    private Entreprise entreprise;
    
    @OneToMany(mappedBy = "offre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Candidature> candidatures = new ArrayList<>();
    
    @OneToMany(mappedBy = "offre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommandation> recommandations = new ArrayList<>();
    
    // Getters, setters, constructeurs...
}