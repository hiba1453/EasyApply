package com.easyapply.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easyapply.entity.Application;
import com.easyapply.entity.Application.ApplicationStatus;
import com.easyapply.entity.Job;
import com.easyapply.entity.User;
import com.easyapply.repository.ApplicationRepository;
import com.easyapply.repository.JobRepository;
import com.easyapply.repository.UserRepository;

@Service
@Transactional
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JobRepository jobRepository;

    // ✅ CRÉATION DE CANDIDATURE
    public Application createApplication(Long userId, Long jobId, String coverLetter) {
        // Vérifications
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Offre d'emploi non trouvée"));

        // Vérifier que l'offre est active
        if (job.getStatus() != Job.JobStatus.ACTIVE) {
            throw new RuntimeException("Cette offre d'emploi n'est plus active");
        }

        // Vérifier si l'utilisateur a déjà postulé
        if (applicationRepository.existsByUser_IdAndJob_Id(userId, jobId)) {
            throw new RuntimeException("Vous avez déjà postulé à cette offre");
        }

        // Créer la candidature
        Application application = new Application(user, job);
        application.setCoverLetter(coverLetter);
        application.setStatus(ApplicationStatus.PENDING);

        return applicationRepository.save(application);
    }

    // ✅ RÉCUPÉRATION DES CANDIDATURES
    public List<Application> getApplicationsByUser(Long userId) {
        return applicationRepository.findByUser_IdOrderByAppliedAtDesc(userId);
    }

    public List<Application> getApplicationsByUserAndStatus(Long userId, ApplicationStatus status) {
        return applicationRepository.findByUser_IdAndStatus(userId, status);
    }

    public List<Application> getApplicationsByJob(Long jobId) {
        return applicationRepository.findByJob_IdOrderByAppliedAtDesc(jobId);
    }

    public List<Application> getApplicationsByEntreprise(Long entrepriseId) {
        return applicationRepository.findByEntrepriseId(entrepriseId);
    }

    public List<Application> getApplicationsByEntrepriseAndStatus(Long entrepriseId, ApplicationStatus status) {
        return applicationRepository.findByEntrepriseIdAndStatus(entrepriseId, status);
    }

    public Optional<Application> getApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId);
    }

    // ✅ GESTION DU STATUT DES CANDIDATURES
    public Application updateApplicationStatus(Long applicationId, ApplicationStatus newStatus, String comment) {
        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Candidature non trouvée"));

        ApplicationStatus oldStatus = application.getStatus();
        application.setStatus(newStatus);

        // Log du changement de statut (simulation)
        System.out.println(String.format("Candidature %d: %s -> %s", 
            applicationId, oldStatus, newStatus));

        return applicationRepository.save(application);
    }

    public Application viewApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Candidature non trouvée"));

        // Si la candidature était en PENDING, la marquer comme VIEWED
        if (application.getStatus() == ApplicationStatus.PENDING) {
            application.setStatus(ApplicationStatus.VIEWED);
            return applicationRepository.save(application);
        }

        return application;
    }

    public Application acceptApplication(Long applicationId, String comment) {
        return updateApplicationStatus(applicationId, ApplicationStatus.ACCEPTED, comment);
    }

    public Application rejectApplication(Long applicationId, String reason) {
        return updateApplicationStatus(applicationId, ApplicationStatus.REJECTED, reason);
    }

    // ✅ RETRAIT DE CANDIDATURE
    public void withdrawApplication(Long applicationId, Long userId) {
        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Candidature non trouvée"));

        // Vérifier que c'est bien l'utilisateur propriétaire
        if (!application.getUser().getId().equals(userId)) {
            throw new RuntimeException("Vous n'êtes pas autorisé à retirer cette candidature");
        }

        // Vérifier que la candidature peut être retirée (seulement PENDING ou VIEWED)
        if (application.getStatus() == ApplicationStatus.ACCEPTED || 
            application.getStatus() == ApplicationStatus.REJECTED) {
            throw new RuntimeException("Cette candidature ne peut plus être retirée");
        }

        applicationRepository.deleteById(applicationId);
    }

    // ✅ STATISTIQUES DES CANDIDATURES
    public Map<String, Object> getApplicationStatsForUser(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        List<Object[]> statusStats = applicationRepository.getApplicationStatsForUser(userId);
        Map<String, Long> statusMap = new HashMap<>();
        
        for (Object[] stat : statusStats) {
            statusMap.put(stat[0].toString(), (Long) stat[1]);
        }
        
        stats.put("total", applicationRepository.countByUserId(userId));
        stats.put("byStatus", statusMap);
        
        // Taux de réponse
        long total = statusMap.values().stream().mapToLong(Long::longValue).sum();
        long responded = statusMap.getOrDefault("VIEWED", 0L) + 
                        statusMap.getOrDefault("ACCEPTED", 0L) + 
                        statusMap.getOrDefault("REJECTED", 0L);
        
        double responseRate = total > 0 ? (double) responded / total : 0.0;
        stats.put("responseRate", responseRate);
        
        return stats;
    }

    public Map<String, Object> getApplicationStatsForEntreprise(Long entrepriseId) {
        Map<String, Object> stats = new HashMap<>();
        
        List<Object[]> statusStats = applicationRepository.getApplicationStatsForEntreprise(entrepriseId);
        Map<String, Long> statusMap = new HashMap<>();
        
        for (Object[] stat : statusStats) {
            statusMap.put(stat[0].toString(), (Long) stat[1]);
        }
        
        stats.put("total", applicationRepository.countByEntrepriseId(entrepriseId));
        stats.put("byStatus", statusMap);
        
        return stats;
    }

    // ✅ CANDIDATURES RÉCENTES
    public List<Application> getRecentApplications(int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return applicationRepository.findRecentApplications(since);
    }

    public List<Application> getRecentApplicationsByUser(Long userId, int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return applicationRepository.findRecentApplicationsByUser(userId, since);
    }

    // ✅ CANDIDATURES EN ATTENTE
    public List<Application> getPendingApplicationsOlderThan(int days) {
        LocalDateTime deadline = LocalDateTime.now().minusDays(days);
        return applicationRepository.findPendingApplicationsOlderThan(deadline);
    }

    // ✅ VALIDATION MÉTIER
    public boolean canUserApplyToJob(Long userId, Long jobId) {
        // Vérifier si l'utilisateur a déjà postulé
        if (applicationRepository.existsByUser_IdAndJob_Id(userId, jobId)) {
            return false;
        }

        // Vérifier si l'offre est active
        Optional<Job> jobOpt = jobRepository.findById(jobId);
        if (jobOpt.isEmpty() || jobOpt.get().getStatus() != Job.JobStatus.ACTIVE) {
            return false;
        }

        return true;
    }

    public boolean canWithdrawApplication(Long applicationId, Long userId) {
        Optional<Application> appOpt = applicationRepository.findById(applicationId);
        if (appOpt.isEmpty()) {
            return false;
        }

        Application app = appOpt.get();
        
        // Vérifier que c'est le bon utilisateur
        if (!app.getUser().getId().equals(userId)) {
            return false;
        }

        // Vérifier le statut
        return app.getStatus() == ApplicationStatus.PENDING || 
               app.getStatus() == ApplicationStatus.VIEWED;
    }

    // ✅ RECHERCHE ET FILTRAGE
    public List<Application> searchApplications(Long entrepriseId, ApplicationStatus status, 
                                              LocalDateTime startDate, LocalDateTime endDate) {
        List<Application> applications = applicationRepository.findByEntrepriseId(entrepriseId);
        
        return applications.stream()
            .filter(app -> status == null || app.getStatus() == status)
            .filter(app -> startDate == null || app.getAppliedAt().isAfter(startDate))
            .filter(app -> endDate == null || app.getAppliedAt().isBefore(endDate))
            .toList();
    }
}