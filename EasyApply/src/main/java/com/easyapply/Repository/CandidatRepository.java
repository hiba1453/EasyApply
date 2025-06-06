package com.easyapply.Repository;

import com.easyapply.entity.Candidat;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatRepository extends JpaRepository<Candidat, Long> {
    Optional<Candidat> findByEmail(String email);
    Optional<Candidat> findById(Long id);
    boolean existsByEmail(String email);
    boolean existsById(Long id);

    void deleteById(Long id);
    void deleteByEmail(String email);
  

    // <-- Add this method
  }
