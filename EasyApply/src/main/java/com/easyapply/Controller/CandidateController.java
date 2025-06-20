package com.easyapply.Controller;


import com.easyapply.entity.*;
import com.easyapply.Repository.*;
import com.easyapply.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/candidat")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CandidateController {

    @Autowired
    private CandidatRepository candidatRepository;

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/profile/{id}")
public ResponseEntity<?> getProfile(@PathVariable Long id) {
    Candidat candidat = candidatRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Candidat non trouvé"));
    Profil profil = candidat.getProfil();
    if (profil == null) {
        return ResponseEntity.ok(new HashMap<>());
    }
    Map<String, Object> response = new HashMap<>();
    response.put("id", profil.getId());
    response.put("candidatId", candidat.getId());
    response.put("nom", candidat.getNom());
    response.put("titreProfessionnel", profil.getTitreProfessionnel());
    response.put("email", candidat.getEmail());
    response.put("telephone", candidat.getTelephone());
    response.put("experiences", profil.getExperiences());
    response.put("formations", profil.getFormations());
    response.put("competences", profil.getCompetences());
    response.put("langues", profil.getLangues());
    return ResponseEntity.ok(response);
}

    @PostMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveProfile(@RequestBody Map<String, Object> profileData, HttpServletRequest request) {
        Profil profil;
        try {
            Long candidatId = getCandidatIdFromRequest(request);
            Candidat candidat = candidatRepository.findById(candidatId)
                .orElseThrow(() -> new Exception("Candidat non trouvé"));

            profil = candidat.getProfil();
            if (profil == null) {
                profil = new Profil();
                profil.setCandidat(candidat);
            }

            profil.setTitreProfessionnel((String) profileData.get("jobTitle"));
            profilRepository.save(profil);

            // Handle experiences
            profil.getExperiences().clear();
            List<?> experiencesRaw = (List<?>) profileData.get("experiences");
            if (experiencesRaw != null) {
                for (Object obj : experiencesRaw) {
                    Map<String, Object> exp = (Map<String, Object>) obj;
                    Experience experience = new Experience();
                    experience.setPoste((String) exp.get("poste"));
                    experience.setEntreprise((String) exp.get("entreprise"));
                    experience.setDateDebut(LocalDate.parse((String) exp.get("dateDebut")));
                    experience.setDateFin(exp.get("dateFin") != null ? LocalDate.parse((String) exp.get("dateFin")) : null);
                    experience.setDescription((String) exp.get("description"));
                    experience.setProfil(profil);
                    profil.getExperiences().add(experience);
                }
            }

            // Handle formations
            profil.getFormations().clear();
            List<?> formationsRaw = (List<?>) profileData.get("formations");
            if (formationsRaw != null) {
                for (Object obj : formationsRaw) {
                    Map<String, Object> edu = (Map<String, Object>) obj;
                    Formation formation = new Formation();
                    formation.setDiplome((String) edu.get("diplome"));
                    formation.setEtablissement((String) edu.get("etablissement"));
                    formation.setDateObtention(LocalDate.parse((String) edu.get("dateObtention")));
                    formation.setProfil(profil);
                    profil.getFormations().add(formation);
                }
            }

            // Handle competences
            profil.getCompetences().clear();
            List<?> competencesRaw = (List<?>) profileData.get("competences");
            if (competencesRaw != null) {
                for (Object obj : competencesRaw) {
                    Map<String, Object> comp = (Map<String, Object>) obj;
                    Competence competence = new Competence();
                    competence.setNom((String) comp.get("nom"));
                    competence.setProfil(profil);
                    profil.getCompetences().add(competence);
                }
            }

            // Handle langues
            profil.getLangues().clear();
            List<?> languesRaw = (List<?>) profileData.get("langues");
            if (languesRaw != null) {
                for (Object obj : languesRaw) {
                    Map<String, Object> lang = (Map<String, Object>) obj;
                    Langue langue = new Langue();
                    langue.setNom((String) lang.get("nom"));
                    langue.setNiveau((String) lang.get("niveau"));
                    langue.setProfil(profil);
                    profil.getLangues().add(langue);
                }
            }

            profilRepository.save(profil);
            return ResponseEntity.ok(mapToResponse(profil));
        }
      catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    private Long getCandidatIdFromRequest(HttpServletRequest request) throws Exception {
        String token = null;
        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split("; ");
            for (String cookie : cookies) {
                if (cookie.startsWith("jwtToken=")) {
                    token = cookie.substring("jwtToken=".length());
                    break;
                }
            }
        }

        if (token == null) {
            throw new Exception("Aucun jeton trouvé");
        }

        Map<String, Object> claims = jwtService.extractClaims(token);
        return ((Number) claims.get("id")).longValue();
    }

    private Map<String, Object> mapToResponse(Profil profil) {
        return Map.of(
            "nom", profil.getCandidat().getNom(),
            "titreProfessionnel", profil.getTitreProfessionnel(),
            "email", profil.getCandidat().getEmail(),
            "location", profil.getCandidat().getTelephone(), // Adjust if location is stored elsewhere
            "experiences", profil.getExperiences(),
            "formations", profil.getFormations(),
            "competences", profil.getCompetences(),
            "langues", profil.getLangues()
        );
    }
}