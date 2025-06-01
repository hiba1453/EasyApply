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
    public Long getId() {
        return id;
    }   
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getMotsCles() {
        return motsCles;
    }
    public void setMotsCles(String motsCles) {
        this.motsCles = motsCles;
    }
    public LocalDateTime getDatePublication() {
        return datePublication;
    }
    public void setDatePublication(LocalDateTime datePublication) {
        this.datePublication = datePublication;
    }
    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }
    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }
    public String getLieu() {
        return lieu;
    }
    public void setLieu(String lieu) {
        this.lieu = lieu;
    }
    public String getSalaire() {
        return salaire;
    }
    public void setSalaire(String salaire) {
        this.salaire = salaire;
    }
    public String getTypeContrat() {
        return typeContrat;
    }
    public void setTypeContrat(String typeContrat) {
        this.typeContrat = typeContrat;
    }
    public String getNiveauExperience() {
        return niveauExperience;
    }
    public void setNiveauExperience(String niveauExperience) {
        this.niveauExperience = niveauExperience;
    }
    public Entreprise getEntreprise() {
        return entreprise;
    }
    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }
    public List<Candidature> getCandidatures() {
        return candidatures;
    }
    public void setCandidatures(List<Candidature> candidatures) {
        this.candidatures = candidatures;
    }
    public List<Recommandation> getRecommandations() {
        return recommandations;
    }
    public void setRecommandations(List<Recommandation> recommandations) {
        this.recommandations = recommandations;
    }
    public OffreEmploi() {
        // Default constructor
    }
    public OffreEmploi(String titre, String description, String motsCles, LocalDateTime dateExpiration, String lieu, String salaire, String typeContrat, String niveauExperience, Entreprise entreprise) {
        this.titre = titre;
        this.description = description;
        this.motsCles = motsCles;
        this.dateExpiration = dateExpiration;
        this.lieu = lieu;
        this.salaire = salaire;
        this.typeContrat = typeContrat;
        this.niveauExperience = niveauExperience;
        this.entreprise = entreprise;
    }
    @Override
    public String toString() {
        return "OffreEmploi{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", motsCles='" + motsCles + '\'' +
                ", datePublication=" + datePublication +
                ", dateExpiration=" + dateExpiration +
                ", lieu='" + lieu + '\'' +
                ", salaire='" + salaire + '\'' +
                ", typeContrat='" + typeContrat + '\'' +
                ", niveauExperience='" + niveauExperience + '\'' +
                ", entreprise=" + entreprise.getNom() +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OffreEmploi)) return false;
        OffreEmploi that = (OffreEmploi) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(titre, that.titre) &&
                Objects.equals(description, that.description) &&
                Objects.equals(motsCles, that.motsCles) &&
                Objects.equals(datePublication, that.datePublication) &&
                Objects.equals(dateExpiration, that.dateExpiration) &&
                Objects.equals(lieu, that.lieu) &&
                Objects.equals(salaire, that.salaire) &&
                Objects.equals(typeContrat, that.typeContrat) &&
                Objects.equals(niveauExperience, that.niveauExperience) &&
                Objects.equals(entreprise, that.entreprise);
    }
}