package com.easyapply.DTO;

import com.easyapply.entity.StatutCandidature;

public class ApplicationStatusRequest {
    private StatutCandidature statut;

    // Constructeurs
    public ApplicationStatusRequest() {}

    public ApplicationStatusRequest(StatutCandidature statut) {
        this.statut = statut;
    }

    // Getters et Setters
    public StatutCandidature getStatut() {
        return statut;
    }

    public void setStatut(StatutCandidature statut) {
        this.statut = statut;
    }
}