package com.easyapply.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.easyapply.entity.Entreprise;

@Repository
public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {
    
    // ✅ AUTHENTIFICATION ENTREPRISE
    Optional<Entreprise> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    // ✅ RECHERCHE PAR SECTEUR
    List<Entreprise> findBySecteurContainingIgnoreCase(String secteur);
    
    // ✅ RECHERCHE PAR NOM
    List<Entreprise> findByNomContainingIgnoreCase(String nom);
    
    // ✅ ENTREPRISES ACTIVES
    List<Entreprise> findByIsActiveTrue();
    
    List<Entreprise> findByIsActiveFalse();
    
    // ✅ VÉRIFICATION SIRET
    Optional<Entreprise> findByNumeroSiret(String numeroSiret);
    
    boolean existsByNumeroSiret(String numeroSiret);
    
    // ✅ STATISTIQUES ENTREPRISES
    @Query("SELECT COUNT(e) FROM Entreprise e WHERE e.isActive = true")
    Long countActiveEntreprises();
    
    @Query("SELECT e.secteur, COUNT(e) FROM Entreprise e WHERE e.isActive = true GROUP BY e.secteur ORDER BY COUNT(e) DESC")
    List<Object[]> getEntreprisesBySecteur();
    
    // ✅ ENTREPRISES AVEC OFFRES ACTIVES
    @Query("SELECT DISTINCT e FROM Entreprise e JOIN e.jobs j WHERE j.status = 'ACTIVE'")
    List<Entreprise> findEntreprisesWithActiveJobs();
    
    @Query("SELECT e, COUNT(j) FROM Entreprise e JOIN e.jobs j WHERE j.status = 'ACTIVE' GROUP BY e ORDER BY COUNT(j) DESC")
    List<Object[]> getEntreprisesWithJobCount();
    
    // ✅ ENTREPRISES RÉCENTES - COMPLÈTEMENT SUPPRIMÉ CETTE MÉTHODE PROBLÉMATIQUE
    // On l'implémentera dans le Service si nécessaire
}
