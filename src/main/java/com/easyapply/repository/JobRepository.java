package com.easyapply.repository;

import com.easyapply.entity.Job;
import com.easyapply.entity.Job.JobStatus;
import com.easyapply.entity.Job.ContractType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
    // ✅ MÉTHODES DE RECHERCHE ET FILTRAGE
    List<Job> findByStatus(JobStatus status);
    
    List<Job> findByStatusAndLocation(JobStatus status, String location);
    
    List<Job> findByStatusAndContractType(JobStatus status, ContractType contractType);
    
    Page<Job> findByStatus(JobStatus status, Pageable pageable);
    
    // ✅ RECHERCHE PAR ENTREPRISE
    List<Job> findByCompanyContainingIgnoreCase(String company);
    
    @Query("SELECT j FROM Job j WHERE j.entreprise.id = :entrepriseId")
    List<Job> findByEntrepriseId(@Param("entrepriseId") Long entrepriseId);
    
    @Query("SELECT j FROM Job j WHERE j.entreprise.id = :entrepriseId AND j.status = :status")
    List<Job> findByEntrepriseIdAndStatus(@Param("entrepriseId") Long entrepriseId, @Param("status") JobStatus status);
    
    // ✅ RECHERCHE PAR LOCALISATION
    List<Job> findByLocationContainingIgnoreCase(String location);
    
    // ✅ RECHERCHE PAR SALAIRE
    List<Job> findBySalaryGreaterThanEqual(BigDecimal minSalary);
    
    List<Job> findBySalaryBetween(BigDecimal minSalary, BigDecimal maxSalary);
    
    // ✅ RECHERCHE TEXTUELLE
    @Query("SELECT j FROM Job j WHERE " +
           "LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.requirements) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Job> findByKeyword(@Param("keyword") String keyword);
    
    // ✅ RECHERCHE COMBINÉE AVANCÉE
    @Query("SELECT j FROM Job j WHERE " +
           "j.status = :status AND " +
           "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:contractType IS NULL OR j.contractType = :contractType) AND " +
           "(:company IS NULL OR LOWER(j.company) LIKE LOWER(CONCAT('%', :company, '%'))) AND " +
           "(:minSalary IS NULL OR j.salary >= :minSalary)")
    Page<Job> findJobsWithFilters(
        @Param("status") JobStatus status,
        @Param("location") String location,
        @Param("contractType") ContractType contractType,
        @Param("company") String company,
        @Param("minSalary") BigDecimal minSalary,
        Pageable pageable
    );
    
    // ✅ STATISTIQUES
    @Query("SELECT COUNT(j) FROM Job j WHERE j.status = :status")
    Long countByStatus(@Param("status") JobStatus status);
    
    @Query("SELECT j.contractType, COUNT(j) FROM Job j WHERE j.status = 'ACTIVE' GROUP BY j.contractType")
    List<Object[]> getContractTypeStats();
    
    @Query("SELECT j.location, COUNT(j) FROM Job j WHERE j.status = 'ACTIVE' GROUP BY j.location ORDER BY COUNT(j) DESC")
    List<Object[]> getTopLocations();
    
    // ✅ OFFRES RÉCENTES
    @Query("SELECT j FROM Job j WHERE j.createdAt >= :since ORDER BY j.createdAt DESC")
    List<Job> findRecentJobs(@Param("since") LocalDateTime since);
    
    // ✅ RECOMMANDATION - MATCHING SIMPLE
    @Query("SELECT j FROM Job j WHERE " +
           "j.status = 'ACTIVE' AND " +
           "(:preferredLocation IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :preferredLocation, '%'))) AND " +
           "(:skills IS NULL OR LOWER(j.requirements) LIKE LOWER(CONCAT('%', :skills, '%')))")
    List<Job> findJobsForRecommendation(
        @Param("preferredLocation") String preferredLocation,
        @Param("skills") String skills
    );
}