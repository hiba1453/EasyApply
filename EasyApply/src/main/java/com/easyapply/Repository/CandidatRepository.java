package com.easyapply.Repository;

import com.easyapply.entity.Candidat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatRepository extends JpaRepository<Candidat, Long> {
    Candidat findByEmail(String email); // <-- Add this method




    
}
