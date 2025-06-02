package com.easyapply.Service;

import com.easyapply.Repository.*;
import com.easyapply.entity.OffreEmploi;
import com.easyapply.DTO.OffreEmploiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OffreEmploiService {
    
  
    @Autowired
    private JobRepository jobRepository;

    public List<OffreEmploi> getAllOffres() {
        return jobRepository.findAll();
    }

    public Optional<OffreEmploi> getOffreById(Long id) {
         return  jobRepository.findById(id);
        
    }

   
    public List<OffreEmploi> getJobsByCompany(Long entrepriseId) {
        return jobRepository.findByEntrepriseIdOrderByDatePublicationDesc(entrepriseId);
    }
    public List<OffreEmploi> searchJobs(String keywords) {
        return jobRepository.findByKeywords(keywords);
    }
    }
