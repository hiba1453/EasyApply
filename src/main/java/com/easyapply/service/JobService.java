package com.easyapply.service;

import com.easyapply.entity.Job;
import com.easyapply.entity.Job.JobStatus;
import com.easyapply.entity.Job.ContractType;
import com.easyapply.entity.Entreprise;
import com.easyapply.repository.JobRepository;
import com.easyapply.repository.EntrepriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@Service
@Transactional
public class JobService {

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private EntrepriseRepository entrepriseRepository;

    // ✅ GESTION DES OFFRES D'EMPLOI
    public List<Job> getAllActiveJobs() {
        return jobRepository.findByStatus(JobStatus.ACTIVE);
    }

    public Page<Job> getAllActiveJobs(Pageable pageable) {
        return jobRepository.findByStatus(JobStatus.ACTIVE, pageable);
    }

    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    // ✅ CRÉATION D'OFFRE D'EMPLOI
    public Job createJob(Long entrepriseId, String title, String description, 
                        String location, String company, BigDecimal salary, 
                        String requirements, ContractType contractType) {
        
        Entreprise entreprise = entrepriseRepository.findById(entrepriseId)
            .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));

        Job job = new Job();
        job.setTitle(title);
        job.setDescription(description);
        job.setLocation(location);
        job.setCompany(company);
        job.setSalary(salary);
        job.setRequirements(requirements);
        job.setContractType(contractType != null ? contractType : ContractType.CDI);
        job.setStatus(JobStatus.ACTIVE);
        job.setEntreprise(entreprise);

        return jobRepository.save(job);
    }

    // ✅ MISE À JOUR D'OFFRE D'EMPLOI
    public Job updateJob(Long jobId, Map<String, Object> updates) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Offre d'emploi non trouvée"));

        if (updates.containsKey("title")) {
            job.setTitle((String) updates.get("title"));
        }
        if (updates.containsKey("description")) {
            job.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("location")) {
            job.setLocation((String) updates.get("location"));
        }
        if (updates.containsKey("salary")) {
            job.setSalary((BigDecimal) updates.get("salary"));
        }
        if (updates.containsKey("requirements")) {
            job.setRequirements((String) updates.get("requirements"));
        }
        if (updates.containsKey("contractType")) {
            job.setContractType(ContractType.valueOf((String) updates.get("contractType")));
        }
        if (updates.containsKey("status")) {
            job.setStatus(JobStatus.valueOf((String) updates.get("status")));
        }

        return jobRepository.save(job);
    }

    // ✅ RECHERCHE ET FILTRAGE D'OFFRES
    public Page<Job> searchJobs(String location, ContractType contractType, 
                               String company, BigDecimal minSalary, Pageable pageable) {
        return jobRepository.findJobsWithFilters(
            JobStatus.ACTIVE, location, contractType, company, minSalary, pageable
        );
    }

    public List<Job> searchJobsByKeyword(String keyword) {
        return jobRepository.findByKeyword(keyword);
    }

    public List<Job> getJobsByLocation(String location) {
        return jobRepository.findByLocationContainingIgnoreCase(location);
    }

    public List<Job> getJobsByCompany(String company) {
        return jobRepository.findByCompanyContainingIgnoreCase(company);
    }

    public List<Job> getJobsBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary) {
        return jobRepository.findBySalaryBetween(minSalary, maxSalary);
    }

    // ✅ GESTION PAR ENTREPRISE
    public List<Job> getJobsByEntreprise(Long entrepriseId) {
        return jobRepository.findByEntrepriseId(entrepriseId);
    }

    public List<Job> getActiveJobsByEntreprise(Long entrepriseId) {
        return jobRepository.findByEntrepriseIdAndStatus(entrepriseId, JobStatus.ACTIVE);
    }

    // ✅ GESTION DU STATUT DES OFFRES
    public Job pauseJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Offre d'emploi non trouvée"));
        
        job.setStatus(JobStatus.PAUSED);
        return jobRepository.save(job);
    }

    public Job activateJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Offre d'emploi non trouvée"));
        
        job.setStatus(JobStatus.ACTIVE);
        return jobRepository.save(job);
    }

    public Job closeJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Offre d'emploi non trouvée"));
        
        job.setStatus(JobStatus.CLOSED);
        return jobRepository.save(job);
    }

    // ✅ STATISTIQUES DES OFFRES
    public Map<String, Object> getJobStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Statistiques par statut
        stats.put("totalJobs", jobRepository.count());
        stats.put("activeJobs", jobRepository.countByStatus(JobStatus.ACTIVE));
        stats.put("pausedJobs", jobRepository.countByStatus(JobStatus.PAUSED));
        stats.put("closedJobs", jobRepository.countByStatus(JobStatus.CLOSED));
        
        // Statistiques par type de contrat
        List<Object[]> contractStats = jobRepository.getContractTypeStats();
        Map<String, Long> contractTypeMap = new HashMap<>();
        for (Object[] stat : contractStats) {
            contractTypeMap.put(stat[0].toString(), (Long) stat[1]);
        }
        stats.put("contractTypeStats", contractTypeMap);
        
        // Top localisations
        List<Object[]> locationStats = jobRepository.getTopLocations();
        stats.put("topLocations", locationStats);
        
        return stats;
    }

    public Map<String, Object> getJobStatsForEntreprise(Long entrepriseId) {
        Map<String, Object> stats = new HashMap<>();
        
        List<Job> jobs = jobRepository.findByEntrepriseId(entrepriseId);
        
        stats.put("totalJobs", jobs.size());
        stats.put("activeJobs", jobs.stream().filter(j -> j.getStatus() == JobStatus.ACTIVE).count());
        stats.put("pausedJobs", jobs.stream().filter(j -> j.getStatus() == JobStatus.PAUSED).count());
        stats.put("closedJobs", jobs.stream().filter(j -> j.getStatus() == JobStatus.CLOSED).count());
        
        // Total candidatures reçues
        long totalApplications = jobs.stream()
            .mapToLong(job -> job.getApplications().size())
            .sum();
        stats.put("totalApplications", totalApplications);
        
        return stats;
    }

    // ✅ OFFRES RÉCENTES
    public List<Job> getRecentJobs(int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return jobRepository.findRecentJobs(since);
    }

    // ✅ RECOMMANDATIONS BASIQUES
    public List<Job> getJobsForUserRecommendation(String preferredLocation, String skills) {
        return jobRepository.findJobsForRecommendation(preferredLocation, skills);
    }

    // ✅ SUPPRESSION D'OFFRE
    public void deleteJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Offre d'emploi non trouvée"));
        
        // Vérifier s'il y a des candidatures
        if (!job.getApplications().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer une offre ayant des candidatures");
        }
        
        jobRepository.deleteById(jobId);
    }

    // ✅ VALIDATION MÉTIER
    public boolean canUserApplyToJob(Long userId, Long jobId) {
        // Logique pour vérifier si un utilisateur peut postuler
        // (pas déjà postulé, offre active, etc.)
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null || job.getStatus() != JobStatus.ACTIVE) {
            return false;
        }
        
        // Vérifier si l'utilisateur a déjà postulé
        return job.getApplications().stream()
            .noneMatch(app -> app.getUser().getId().equals(userId));
    }
}