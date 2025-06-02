package com.easyapply.DTO;

import java.time.LocalDateTime;

public class JobRequest {
    private Long entrepriseId;
    private String titre;
    private String description;
    private String lieu;
    private String typeContrat;
    private String niveauExperience;
    private String salaire;
    private String motsCles;
    private LocalDateTime dateExpiration;

    // Constructeurs
    public JobRequest() {}

    public JobRequest(String titre, String description, String lieu, String salaire, Long entrepriseId) {
        this.titre = titre;
        this.description = description;
        this.lieu = lieu;
        this.salaire = salaire;
        this.entrepriseId = entrepriseId;
    }

    // Getters et Setters
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

    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Long getEntrepriseId() {
        return entrepriseId;
    }

    public void setEntrepriseId(Long entrepriseId) {
        this.entrepriseId = entrepriseId;
    }
}