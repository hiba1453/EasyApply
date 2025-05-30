package com.easyapply.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easyapply.entity.Recommendation;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    
    // ✅ RECHERCHE PAR UTILISATEUR
    List<Recommendation> findByUser_Id(Long userId);
    
    List<Recommendation> findByUser_IdOrderByMatchScoreDesc(Long userId);
    
    List<Recommendation> findByUser_IdAndIsViewedFalse(Long userId);
    
    List<Recommendation> findByUser_IdAndIsViewedTrue(Long userId);
    
    // ✅ RECHERCHE PAR OFFRE
    List<Recommendation> findByJob_Id(Long jobId);
    
    // ✅ RECHERCHE PAR SCORE
    List<Recommendation> findByMatchScoreGreaterThanEqual(Double minScore);
    
    @Query("SELECT r FROM Recommendation r WHERE r.user.id = :userId AND r.matchScore >= :minScore ORDER BY r.matchScore DESC")
    List<Recommendation> findByUserIdAndMinScore(@Param("userId") Long userId, @Param("minScore") Double minScore);
    
    // ✅ SUPPRESSION EN MASSE
    @Modifying
    @Query("DELETE FROM Recommendation r WHERE r.user.id = :userId")
    void deleteByUser_Id(@Param("userId") Long userId);
    
    @Modifying
    @Query("DELETE FROM Recommendation r WHERE r.job.id = :jobId")
    void deleteByJob_Id(@Param("jobId") Long jobId);
    
    // ✅ STATISTIQUES
    @Query("SELECT COUNT(r) FROM Recommendation r WHERE r.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(r) FROM Recommendation r WHERE r.user.id = :userId AND r.isViewed = false")
    Long countUnviewedByUserId(@Param("userId") Long userId);
    
    // ✅ RECOMMANDATIONS RÉCENTES
    @Query("SELECT r FROM Recommendation r WHERE r.createdAt >= :since ORDER BY r.createdAt DESC")
    List<Recommendation> findRecentRecommendations(@Param("since") LocalDateTime since);
    
    @Query("SELECT r FROM Recommendation r WHERE r.user.id = :userId AND r.createdAt >= :since ORDER BY r.createdAt DESC")
    List<Recommendation> findRecentRecommendationsByUser(@Param("userId") Long userId, @Param("since") LocalDateTime since);
    
    // ✅ TOP RECOMMANDATIONS PAR SCORE
    @Query("SELECT r FROM Recommendation r WHERE r.user.id = :userId ORDER BY r.matchScore DESC")
    List<Recommendation> findTopRecommendationsByUser(@Param("userId") Long userId);
}