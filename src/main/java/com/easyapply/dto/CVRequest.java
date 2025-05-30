package com.easyapply.dto;
import java.util.List;

public class CVRequest {
    private String nom;
    private String email;
    private String localisation;
    private String titreProfessionnel;

    private List<String> competences;
    private List<experienceDTO> experiences;
    private List<FormationDTO> formations;
    private List<LangueDTO> langues;

    // Getters & Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }

    public String getTitreProfessionnel() { return titreProfessionnel; }
    public void setTitreProfessionnel(String titreProfessionnel) { this.titreProfessionnel = titreProfessionnel; }

    public List<String> getCompetences() { return competences; }
    public void setCompetences(List<String> competences) { this.competences =
        competences; }

    public List<experienceDTO> getExperiences() { return experiences; }
    public void setExperiences(List<experienceDTO> experiences) { this.experiences = experiences; }

    public List<FormationDTO> getFormations() { return formations; }
    public void setFormations(List<FormationDTO> formations) { this.formations = formations; }

    public List<LangueDTO> getLangues() { return langues; }
    public void setLangues(List<LangueDTO> langues) { this.langues = langues; }

    

}