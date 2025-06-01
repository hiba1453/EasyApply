package com.easyapply.DTO;

public class RegisterRequest {
    private String nom;
    private String email;
    private String motDePasse;
    private String confirmedMotDePasse;
    private String telephone;
    private String dateNaissance; // Use String for easier JSON mapping, then parse to LocalDate

    // Getters and setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getConfirmedMotDePasse() { return confirmedMotDePasse; }
    public void setConfirmedMotDePasse(String confirmedMotDePasse) { this.confirmedMotDePasse = confirmedMotDePasse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }
}
