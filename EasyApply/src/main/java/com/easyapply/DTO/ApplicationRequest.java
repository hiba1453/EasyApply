package com.easyapply.DTO;

public class ApplicationRequest {
    private Long candidatId;
    private Long offreId;
    private Long cvId;

    // Constructeurs
    public ApplicationRequest() {}

    public ApplicationRequest(Long candidatId, Long offreId, Long cvId) {
        this.candidatId = candidatId;
        this.offreId = offreId;
        this.cvId = cvId;
    }

    // Getters et Setters
    public Long getCandidatId() {
        return candidatId;
    }

    public void setCandidatId(Long candidatId) {
        this.candidatId = candidatId;
    }

    public Long getOffreId() {
        return offreId;
    }

    public void setOffreId(Long offreId) {
        this.offreId = offreId;
    }

    public Long getCvId() {
        return cvId;
    }

    public void setCvId(Long cvId) {
        this.cvId = cvId;
    }
}
