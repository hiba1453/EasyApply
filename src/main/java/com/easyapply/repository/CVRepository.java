package com.easyapply.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easyapply.entity.CV;
import com.easyapply.entity.CV.AnalysisStatus;

@Repository
public interface CVRepository extends JpaRepository<CV, Long> {
    
    // ✅ MÉTHODES DE RECHERCHE PAR UTILISATEUR
    List<CV> findByUser_Id(Long userId);
    
    List<CV> findByUser_IdOrderByUploadedAtDesc(Long userId);
    
    Optional<CV> findByUser_IdAndIsPrimaryTrue(Long userId);
    
    List<CV> findByUser_IdAndIsAnalyzedTrue(Long userId);
    
    // ✅ MÉTHODES PAR STATUT D'ANALYSE
    List<CV> findByAnalysisStatus(AnalysisStatus status);
    
    List<CV> findByUser_IdAndAnalysisStatus(Long userId, AnalysisStatus status);
    
    // ✅ CV EN ATTENTE D'ANALYSE
    @Query("SELECT c FROM CV c WHERE c.analysisStatus = 'PENDING' ORDER BY c.uploadedAt ASC")
    List<CV> findPendingAnalysis();
    
    // ✅ STATISTIQUES
    @Query("SELECT COUNT(c) FROM CV c WHERE c.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(c) FROM CV c WHERE c.user.id = :userId AND c.isAnalyzed = true")
    Long countAnalyzedByUserId(@Param("userId") Long userId);
    
    // ✅ CV RÉCENTS
    @Query("SELECT c FROM CV c WHERE c.uploadedAt >= :since ORDER BY c.uploadedAt DESC")
    List<CV> findRecentCVs(@Param("since") LocalDateTime since);
    
    @Query("SELECT c FROM CV c WHERE c.user.id = :userId AND c.uploadedAt >= :since ORDER BY c.uploadedAt DESC")
    List<CV> findRecentCVsByUser(@Param("userId") Long userId, @Param("since") LocalDateTime since);
    
    // ✅ RECHERCHE PAR NOM DE FICHIER
    List<CV> findByFileNameContainingIgnoreCase(String fileName);
    
    List<CV> findByUser_IdAndFileNameContainingIgnoreCase(Long userId, String fileName);
    
    // ✅ CV AVEC COMPÉTENCES EXTRAITES
    @Query("SELECT c FROM CV c WHERE c.extractedSkills IS NOT NULL AND c.extractedSkills != ''")
    List<CV> findCVsWithExtractedSkills();
    
    @Query("SELECT c FROM CV c WHERE c.user.id = :userId AND c.extractedSkills IS NOT NULL AND c.extractedSkills != ''")
    List<CV> findCVsWithExtractedSkillsByUser(@Param("userId") Long userId);
}