package com.easyapply.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyapply.entity.Candidat;

public interface CandidatRepository extends JpaRepository<Candidat, Long> {
    
    // Tu peux ajouter des méthodes personnalisées ici si besoin
}
