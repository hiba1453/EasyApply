package com.easyapply.Service;

import com.easyapply.DTO.RegisterRequest;
import com.easyapply.entity.Candidat;
import com.easyapply.Repository.CandidatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CandidatService {

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
}
