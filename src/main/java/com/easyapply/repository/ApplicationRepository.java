package com.easyapply.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easyapply.entity.Application;
import com.easyapply.entity.Application.ApplicationStatus;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    // ✅ CORRECTED - Utilisé les relations JPA au lieu des IDs
    List<Application> findByUser_Id(Long userId);
    
    List<Application> findByUser_IdAndStatus(Long userId, ApplicationStatus status);
    
    List<Application> findByUser_IdOrderByAppliedAtDesc(Long userId);
    
    // ✅ RECHERCHE PAR OFFRE D'EMPLOI
    List<Application> findByJob_Id(Long jobId);
    
    List<Application> findByJob_IdAndStatus(Long jobId, ApplicationStatus status);
    
    List<Application> findByJob_IdOrderByAppliedAtDesc(Long jobId);
    
    // ✅ RECHERCHE PAR ENTREPRISE
    @Query("SELECT a FROM Application a WHERE a.job.entreprise.id = :entrepriseId")
    List<Application> findByEntrepriseId(@Param("entrepriseId") Long entrepriseId);
    
    @Query("SELECT a FROM Application a WHERE a.job.entreprise.id = :entrepriseId AND a.status = :status")
    List<Application> findByEntrepriseIdAndStatus(@Param("entrepriseId") Long entrepriseId, @Param("status") ApplicationStatus status);
    
    // ✅ VÉRIFICATIONS DE DOUBLONS
    boolean existsByUser_IdAndJob_Id(Long userId, Long jobId);
    
    Optional<Application> findByUser_IdAndJob_Id(Long userId, Long jobId);
    
    // ✅ STATISTIQUES PAR UTILISATEUR
    @Query("SELECT COUNT(a) FROM Application a WHERE a.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT a.status, COUNT(a) FROM Application a WHERE a.user.id = :userId GROUP BY a.status")
    List<Object[]> getApplicationStatsForUser(@Param("userId") Long userId);
    
    // ✅ STATISTIQUES PAR ENTREPRISE
    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.entreprise.id = :entrepriseId")
    Long countByEntrepriseId(@Param("entrepriseId") Long entrepriseId);
    
    @Query("SELECT a.status, COUNT(a) FROM Application a WHERE a.job.entreprise.id = :entrepriseId GROUP BY a.status")
    List<Object[]> getApplicationStatsForEntreprise(@Param("entrepriseId") Long entrepriseId);
    
    // ✅ CANDIDATURES RÉCENTES
    @Query("SELECT a FROM Application a WHERE a.appliedAt >= :since ORDER BY a.appliedAt DESC")
    List<Application> findRecentApplications(@Param("since") LocalDateTime since);
    
    @Query("SELECT a FROM Application a WHERE a.user.id = :userId AND a.appliedAt >= :since ORDER BY a.appliedAt DESC")
    List<Application> findRecentApplicationsByUser(@Param("userId") Long userId, @Param("since") LocalDateTime since);
    
    // ✅ CANDIDATURES EN ATTENTE DE RÉPONSE
    @Query("SELECT a FROM Application a WHERE a.status = 'PENDING' AND a.appliedAt <= :deadline")
    List<Application> findPendingApplicationsOlderThan(@Param("deadline") LocalDateTime deadline);
    
    // ✅ STATISTIQUES GLOBALES
    @Query("SELECT a.status, COUNT(a) FROM Application a GROUP BY a.status")
    List<Object[]> getGlobalApplicationStats();
    
    @Query("SELECT FUNCTION('DATE', a.appliedAt), COUNT(a) FROM Application a " +
           "WHERE a.appliedAt >= :startDate GROUP BY FUNCTION('DATE', a.appliedAt) ORDER BY FUNCTION('DATE', a.appliedAt)")
    List<Object[]> getApplicationsByDate(@Param("startDate") LocalDateTime startDate);
    
    // ✅ TOP OFFRES PAR NOMBRE DE CANDIDATURES
    @Query("SELECT a.job, COUNT(a) FROM Application a GROUP BY a.job ORDER BY COUNT(a) DESC")
    List<Object[]> getTopJobsByApplicationCount();
}