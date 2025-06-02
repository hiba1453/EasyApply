package com.easyapply.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easyapply.DTO.JobRequest;
import com.easyapply.Repository.JobRepository;
import com.easyapply.Repository.CompanyRepository;
import com.easyapply.entity.OffreEmploi;
import com.easyapply.entity.Entreprise;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public OffreEmploi createJob(JobRequest request) {
        // Vérifier que l'entreprise existe
        Optional<Entreprise> entreprise = companyRepository.findById(request.getEntrepriseId());
        if (!entreprise.isPresent()) {
            throw new RuntimeException("Entreprise non trouvée");
        }

        OffreEmploi job = new OffreEmploi();
         // Remplacez par l'ID de l'entreprise approprié
        job.setTitre(request.getTitre());
        job.setDescription(request.getDescription());
        job.setMotsCles(request.getMotsCles());
        job.setLieu(request.getLieu());
        job.setSalaire(request.getSalaire());
        job.setTypeContrat(request.getTypeContrat());
        job.setNiveauExperience(request.getNiveauExperience());
        job.setDateExpiration(request.getDateExpiration());
        job.setEntreprise(entreprise.get());
        job.setDatePublication(LocalDateTime.now());

        return jobRepository.save(job);
    }

    public List<OffreEmploi> getJobsByCompany(Long entrepriseId) {
        return jobRepository.findByEntrepriseIdOrderByDatePublicationDesc(entrepriseId);
    }

    public Optional<OffreEmploi> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    public Optional<OffreEmploi> updateJob(Long id, JobRequest request) {
        Optional<OffreEmploi> optionalJob = jobRepository.findById(id);
        if (!optionalJob.isPresent()) {
            return Optional.empty();
        }

        OffreEmploi job = optionalJob.get();
        
        if (request.getTitre() != null) {
            job.setTitre(request.getTitre());
        }
        if (request.getDescription() != null) {
            job.setDescription(request.getDescription());
        }
        if (request.getMotsCles() != null) {
            job.setMotsCles(request.getMotsCles());
        }
        if (request.getLieu() != null) {
            job.setLieu(request.getLieu());
        }
        if (request.getSalaire() != null) {
            job.setSalaire(request.getSalaire());
        }
        if (request.getTypeContrat() != null) {
            job.setTypeContrat(request.getTypeContrat());
        }
        if (request.getNiveauExperience() != null) {
            job.setNiveauExperience(request.getNiveauExperience());
        }
        if (request.getDateExpiration() != null) {
            job.setDateExpiration(request.getDateExpiration());
        }

        return Optional.of(jobRepository.save(job));
    }

    public boolean deleteJob(Long id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<OffreEmploi> getAllJobs() {
        return jobRepository.findActiveJobs();
    }

    public List<OffreEmploi> searchJobs(String keywords) {
        return jobRepository.findByKeywords(keywords);
    }

    public Long getApplicationCount(Long jobId) {
        return jobRepository.countApplicationsByJobId(jobId);
    }

    public List<OffreEmploi> getJobsByLocation(String location) {
        return jobRepository.findByLocation(location);
    }

    public List<OffreEmploi> getJobsByContractType(String type) {
        return jobRepository.findByContractType(type);
    }
}