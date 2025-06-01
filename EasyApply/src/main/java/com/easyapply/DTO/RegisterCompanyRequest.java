package com.easyapply.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterCompanyRequest {
    
    @NotBlank(message = "Le nom de l'entreprise est requis")
    private String nom;
    
    @NotBlank(message = "L'email est requis")
    @Email(message = "L'email doit être valide")
    private String email;
    
    @NotBlank(message = "Le mot de passe est requis")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String motDePasse;
    
    @NotBlank(message = "Le secteur d'activité est requis")
    private String secteur;
    
    private String description; // Optionnel
    
    // Constructeurs
    public RegisterCompanyRequest() {}
    
    public RegisterCompanyRequest(String nom, String email, String motDePasse, String secteur) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.secteur = secteur;
    }
    
    // Getters et Setters
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