package com.easyapply.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easyapply.DTO.ApplicationRequest;
import com.easyapply.Repository.ApplicationRepository;
import com.easyapply.Repository.CandidatRepository;
import com.easyapply.Repository.JobRepository;
import com.easyapply.entity.Candidat;
import com.easyapply.entity.Candidature;
import com.easyapply.entity.OffreEmploi;
import com.easyapply.entity.StatutCandidature;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CandidatRepository candidatRepository;

    @Autowired
    private JobRepository jobRepository;

    public Candidature createApplication(ApplicationRequest request) {
        // Vérifier que le candidat et l'offre existent
        Optional<Candidat> candidat = candidatRepository.findById(request.getCandidatId());
        Optional<OffreEmploi> offre = jobRepository.findById(request.getOffreId());

        if (!candidat.isPresent()) {
            throw new RuntimeException("Candidat non trouvé");
        }
        if (!offre.isPresent()) {
            throw new RuntimeException("Offre non trouvée");
        }

        // Vérifier si une candidature existe déjà
        if (applicationRepository.existsByCandidatIdAndOffreId(request.getCandidatId(), request.getOffreId())) {
            throw new RuntimeException("Vous avez déjà postulé à cette offre");
        }

        Candidature candidature = new Candidature();
        candidature.setCandidat(candidat.get());
        candidature.setOffre(offre.get());
        candidature.setDatePostulation(LocalDateTime.now());
        candidature.setStatut(StatutCandidature.EN_ATTENTE);

        return applicationRepository.save(candidature);
    }

    public List<Candidature> getApplicationsByJob(Long jobId) {
        return applicationRepository.findByOffreIdOrderByDatePostulationDesc(jobId);
    }

    public List<Candidature> getApplicationsByCandidate(Long candidateId) {
        return applicationRepository.findByCandidatIdOrderByDatePostulationDesc(candidateId);
    }

    public List<Candidature> getApplicationsByCompany(Long companyId) {
        return applicationRepository.findByCompanyIdOrderByDatePostulationDesc(companyId);
    }

    public Candidature updateApplicationStatus(Long candidateId, Long jobId, StatutCandidature statut) {
        Optional<Candidature> optionalApplication = applicationRepository.findByCandidatIdAndOffreId(candidateId, jobId);
        if (!optionalApplication.isPresent()) {
            return null;
        }

        Candidature candidature = optionalApplication.get();
        candidature.setStatut(statut);
        
        return applicationRepository.save(candidature);
    }

    public Candidature getApplication(Long candidateId, Long jobId) {
        Optional<Candidature> candidature = applicationRepository.findByCandidatIdAndOffreId(candidateId, jobId);
        return candidature.orElse(null);
    }

    public boolean deleteApplication(Long candidateId, Long jobId) {
        Optional<Candidature> candidature = applicationRepository.findByCandidatIdAndOffreId(candidateId, jobId);
        if (candidature.isPresent()) {
            applicationRepository.delete(candidature.get());
            return true;
        }
        return false;
    }

    public Long getApplicationCountByJob(Long jobId) {
        return applicationRepository.countByOffreId(jobId);
    }

    public Long getApplicationCountByCompanyAndStatus(Long companyId, StatutCandidature statut) {
        return applicationRepository.countByCompanyIdAndStatut(companyId, statut);
    }

    public List<Candidature> getApplicationsByCompanyAndStatus(Long companyId, StatutCandidature statut) {
        return applicationRepository.findByCompanyIdAndStatut(companyId, statut);
    }
}
