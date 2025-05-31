package com.easyapply.entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.*;



import jakarta.persistence.*;



@Entity
@Table(name = "entreprise")
public class Entreprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String motDePasse;
    
    private String secteur;
    private String description;
 
   
    
    @OneToMany(mappedBy = "entreprise", cascade = {CascadeType.ALL}, orphanRemoval = true)
    @OrderBy("datePublication DESC")
    private List<OffreEmploi> offres = new ArrayList<>();
    
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
    public String getSecteur() {
        return secteur;
    }
    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
 