package com.easyapply.entity;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "recommandation")
public class Recommandation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Double score;
    
    @Column(nullable = false)
    private LocalDateTime dateRecommandation = LocalDateTime.now();
    
    @ManyToOne
    @JoinColumn(name = "candidat_id", nullable = false)
    private Candidat candidat;
    
    @ManyToOne
    @JoinColumn(name = "offre_id", nullable = false)
    private OffreEmploi offre;
    
    // Getters, setters, constructeurs...
    
    @PrePersist
    @PreUpdate
    public void checkUniqueRecommandation() {
        // Vérifier l'unicité de la recommandation pour ce candidat et cette offre
    }
}