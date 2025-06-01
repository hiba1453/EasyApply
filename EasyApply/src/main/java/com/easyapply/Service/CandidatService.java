package com.easyapply.Service;

import com.easyapply.DTO.RegisterRequest;
import com.easyapply.entity.Candidat;
import com.easyapply.Repository.CandidatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.easyapply.DTO.LoginRequest;
import com.easyapply.Service.JwtService;
import java.util.HashMap;
import java.util.*;

import java.time.LocalDate;

@Service
public class CandidatService {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private CandidatRepository candidatRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Candidat registerCandidat(RegisterRequest request) {
        // Validation déjà faite dans le contrôleur
        LocalDate dateNaissance = LocalDate.parse(request.getDateNaissance());

        Candidat candidat = new Candidat();
        candidat.setNom(request.getNom());
        candidat.setEmail(request.getEmail());
        candidat.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        candidat.setTelephone(request.getTelephone());
        candidat.setDateNaissance(dateNaissance);

        return candidatRepository.save(candidat);
    }

    public Optional<Candidat> findByEmail(String email) {
        return candidatRepository.findByEmail(email);
    }
    public boolean existsByEmail(String email) {
        return candidatRepository.existsByEmail(email);
    }
    public Candidat findById(Long id) {
        return candidatRepository.findById(id).orElse(null);
    }
   
       // Service pour gérer les tokens

    public String login(LoginRequest request) {
        // Recherche le candidat
        Candidat candidat = candidatRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect"));
            

        // Vérifie le mot de passe
        if (!passwordEncoder.matches(request.getMotDePasse(), candidat.getMotDePasse())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        // Génère le token JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", candidat.getId());
        claims.put("email", candidat.getEmail());
        claims.put("role", "CANDIDAT");

        return jwtService.generateToken(claims, candidat.getEmail());
    }

}
