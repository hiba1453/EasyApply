package com.easyapply.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easyapply.entity.OffreEmploi;

@Repository
public interface JobRepository extends JpaRepository<OffreEmploi, Long> {
    
    List<OffreEmploi> findByEntrepriseIdOrderByDatePublicationDesc(Long entrepriseId);
    
    @Query("SELECT o FROM OffreEmploi o WHERE " +
           "LOWER(o.titre) LIKE LOWER(CONCAT('%', :keywords, '%')) OR " +
           "LOWER(o.description) LIKE LOWER(CONCAT('%', :keywords, '%')) OR " +
           "LOWER(o.motsCles) LIKE LOWER(CONCAT('%', :keywords, '%'))")
    List<OffreEmploi> findByKeywords(@Param("keywords") String keywords);
    
    @Query("SELECT o FROM OffreEmploi o WHERE o.lieu LIKE %:location%")
    List<OffreEmploi> findByLocation(@Param("location") String location);
    
    @Query("SELECT o FROM OffreEmploi o WHERE o.typeContrat = :type")
    List<OffreEmploi> findByContractType(@Param("type") String type);
    
    @Query("SELECT COUNT(c) FROM Candidature c WHERE c.offre.id = :jobId")
    Long countApplicationsByJobId(@Param("jobId") Long jobId);
    
    @Query("SELECT o FROM OffreEmploi o WHERE o.dateExpiration > CURRENT_TIMESTAMP OR o.dateExpiration IS NULL")
    List<OffreEmploi> findActiveJobs();
}