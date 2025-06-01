package com.easyapply.entity;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "candidature")
@IdClass(CandidatureId.class)

public class Candidature {
   @Id
    @ManyToOne
    @JoinColumn(name = "candidat_id", nullable = false)
    private Candidat candidat;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "offre_id", nullable = false)
    private OffreEmploi offre;
    
    @ManyToOne
    @JoinColumn(name = "cv_id")
    private CV cv;
    
    @Column(nullable = false)
    private LocalDateTime datePostulation = LocalDateTime.now();
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutCandidature statut = StatutCandidature.EN_ATTENTE;

    // Remplacez la méthode setCv problématique dans votre classe Candidature.java par :

public void setCv(CV cv) {
    this.cv = cv;
}

// Et ajoutez ces getters/setters si ils ne sont pas déjà présents :

public Candidat getCandidat() {
    return candidat;
}

public void setCandidat(Candidat candidat) {
    this.candidat = candidat;
}

public OffreEmploi getOffre() {
    return offre;
}

public void setOffre(OffreEmploi offre) {
    this.offre = offre;
}

public CV getCv() {
    return cv;
}

public LocalDateTime getDatePostulation() {
    return datePostulation;
}

public void setDatePostulation(LocalDateTime datePostulation) {
    this.datePostulation = datePostulation;
}

public StatutCandidature getStatut() {
    return statut;
}

public void setStatut(StatutCandidature statut) {
    this.statut = statut;
}

@Override
public String toString() {
    return "Candidature{" +
            "candidat=" + (candidat != null ? candidat.getId() : null) +
            ", offre=" + (offre != null ? offre.getId() : null) +
            ", cv=" + (cv != null ? cv.getId() : null) +
            ", datePostulation=" + datePostulation +
            ", statut=" + statut +
            '}';
}

@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Candidature)) return false;

    Candidature that = (Candidature) o;

    if (!candidat.equals(that.candidat)) return false;
    if (!offre.equals(that.offre)) return false;
    return cv != null ? cv.equals(that.cv) : that.cv == null;
}

@Override
public int hashCode() {
    int result = candidat.hashCode();
    result = 31 * result + offre.hashCode();
    result = 31 * result + (cv != null ? cv.hashCode() : 0);
    return result;
}}