package com.easyapply.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easyapply.entity.Entreprise;
import com.easyapply.Repository.CompanyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EntrepriseService {
    
    @Autowired
    private CompanyRepository companyRepository;
    
    public Entreprise save(Entreprise entreprise) {
        return companyRepository.save(entreprise);
    }
    
    public Optional<Entreprise> findById(Long id) {
        return companyRepository.findById(id);
    }
    
    public Optional<Entreprise> findByEmail(String email) {
        return companyRepository.findByEmail(email);
    }
    
    public boolean existsByEmail(String email) {
        return companyRepository.existsByEmail(email);
    }
    
    public List<Entreprise> findAll() {
        return companyRepository.findAll();
    }
    
    public void deleteById(Long id) {
        companyRepository.deleteById(id);
    }
}