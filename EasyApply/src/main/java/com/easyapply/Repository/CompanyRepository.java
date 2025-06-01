package com.easyapply.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easyapply.entity.Entreprise;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Entreprise, Long> {
    
    Optional<Entreprise> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    // JpaRepository fournit automatiquement :
    // - save(Entreprise entreprise)
    // - findById(Long id) 
    // - findAll()
    // - deleteById(Long id)
}