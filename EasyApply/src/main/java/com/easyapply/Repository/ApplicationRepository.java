package com.easyapply.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easyapply.entity.Candidature;
import com.easyapply.entity.CandidatureId;
import com.easyapply.entity.StatutCandidature;

@Repository
public interface ApplicationRepository extends JpaRepository<Candidature, CandidatureId> {
    
    List<Candidature> findByCandidatIdOrderByDatePostulationDesc(Long candidatId);
    
    List<Candidature> findByOffreIdOrderByDatePostulationDesc(Long offreId);
    
    @Query("SELECT c FROM Candidature c WHERE c.offre.entreprise.id = :companyId ORDER BY c.datePostulation DESC")
    List<Candidature> findByCompanyIdOrderByDatePostulationDesc(@Param("companyId") Long companyId);
    
    @Query("SELECT c FROM Candidature c WHERE c.candidat.id = :candidatId AND c.offre.id = :offreId")
    Optional<Candidature> findByCandidatIdAndOffreId(@Param("candidatId") Long candidatId, @Param("offreId") Long offreId);
    
    @Query("SELECT COUNT(c) FROM Candidature c WHERE c.offre.id = :offreId")
    Long countByOffreId(@Param("offreId") Long offreId);
    
    @Query("SELECT COUNT(c) FROM Candidature c WHERE c.offre.entreprise.id = :companyId AND c.statut = :statut")
    Long countByCompanyIdAndStatut(@Param("companyId") Long companyId, @Param("statut") StatutCandidature statut);
    
    @Query("SELECT c FROM Candidature c WHERE c.offre.entreprise.id = :companyId AND c.statut = :statut")
    List<Candidature> findByCompanyIdAndStatut(@Param("companyId") Long companyId, @Param("statut") StatutCandidature statut);
    
    boolean existsByCandidatIdAndOffreId(Long candidatId, Long offreId);
}