package com.easyapply.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.easyapply.entity.Entreprise;
import com.easyapply.Repository.CompanyRepository;

import com.easyapply.DTO.RegisterCRequest;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.easyapply.entity.Entreprise;
import com.easyapply.Repository.CompanyRepository;
import com.easyapply.DTO.RegisterCRequest;

import java.util.List;
import java.util.Optional;

@Service
public class EntrepriseService {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
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

    public Entreprise registerEntreprise(RegisterCRequest request) {  // Change parameter type
        Entreprise entreprise = new Entreprise();
        entreprise.setNom(request.getNom());
        entreprise.setEmail(request.getEmail());
        entreprise.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        entreprise.setSecteur(request.getSecteur());
        entreprise.setDescription(request.getDescription());
        return companyRepository.save(entreprise);
    }
}