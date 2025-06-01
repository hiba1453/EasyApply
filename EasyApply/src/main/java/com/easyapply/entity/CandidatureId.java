package com.easyapply.entity;

import java.io.Serializable;
import java.util.Objects;

public class CandidatureId implements Serializable {
    private Long candidat;  // Correspond au candidat.id
    private Long offre;     // Correspond au offre.id
    
    public CandidatureId() {}
    
    public CandidatureId(Long candidat, Long offre) {
        this.candidat = candidat;
        this.offre = offre;
    }
    
    // Getters et setters
    public Long getCandidat() {
        return candidat;
    }
    
    public void setCandidat(Long candidat) {
        this.candidat = candidat;
    }
    
    public Long getOffre() {
        return offre;
    }
    
    public void setOffre(Long offre) {
        this.offre = offre;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidatureId)) return false;
        CandidatureId that = (CandidatureId) o;
        return Objects.equals(candidat, that.candidat) && 
               Objects.equals(offre, that.offre);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(candidat, offre);
    }
}