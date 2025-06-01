package com.easyapply.DTO;

public class RegisterCRequest {
     private String nom;
    private String email;
    private String motDePasse;
    private String secteur;
    private String description;

  
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getSecteur() { return secteur; }
    public void setSecteur(String secteur) { this.secteur = secteur; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }


}
