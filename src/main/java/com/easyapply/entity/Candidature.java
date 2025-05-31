package com.easyapply.entity;
import java.time.LocalDateTime;


import jakarta.persistence.*;


@Entity
@Table(name = "candidature")

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

    public void setCv(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCv'");
    }
    
    // Getters, setters, constructeurs...
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
    public void setCv(CV cv) {
        this.cv = cv;
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
                "candidat=" + candidat +
                ", offre=" + offre +
                ", cv=" + cv +
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


}

