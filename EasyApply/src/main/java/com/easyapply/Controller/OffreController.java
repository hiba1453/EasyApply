package com.easyapply.Controller;

import com.easyapply.Service.JwtService;
import com.easyapply.entity.CV;
import com.easyapply.entity.Candidat;
import com.easyapply.entity.Candidature;
import com.easyapply.entity.OffreEmploi;
import com.easyapply.entity.StatutCandidature;
import com.easyapply.Repository.CvRepository;
import com.easyapply.Repository.CandidatRepository;
import com.easyapply.Repository.CandidatureRepository;
import com.easyapply.Repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/offre")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class OffreController {

    @Autowired
    private JobRepository offreEmploiRepository;

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired
    private CandidatRepository candidatRepository;

    @Autowired
    private CvRepository cvRepository;

    @Autowired
    private JwtService jwtService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOffreDetails(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long candidatId = getCandidatIdFromRequest(request);
            System.out.println("Fetching offer for candidatId: " + candidatId + ", offerId: " + id);

            Optional<OffreEmploi> offre = offreEmploiRepository.findById(id);
            if (offre.isEmpty()) {
                return ResponseEntity.status(404)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "Offre d'emploi non trouvée"));
            }

            boolean hasApplied = candidatureRepository.findByCandidatIdAndOffreId(candidatId, id).isPresent();

            Map<String, Object> response = Map.of(
                "offre", offre.get(),
                "hasApplied", hasApplied
            );
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
        } catch (Exception e) {
            System.err.println("Error in getOffreDetails: " + e.getMessage());
            return ResponseEntity.status(401)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("error", "Session invalide: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/{id}/apply")
    public ResponseEntity<?> applyToOffre(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long candidatId = getCandidatIdFromRequest(request);
            
            Optional<Candidat> candidat = candidatRepository.findById(candidatId);
            if (candidat.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(Map.of("error", "Candidat non trouvé"));
            }

            // Check if candidate already has a CV
            Optional<CV> existingCV = cvRepository.findByCandidatId(candidatId);
            if (existingCV.isEmpty()) {
                return ResponseEntity.status(400)
                    .body(Map.of("error", "Veuillez d'abord ajouter votre CV dans votre profil"));
            }

            Optional<OffreEmploi> offre = offreEmploiRepository.findById(id);
            if (offre.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(Map.of("error", "Offre d'emploi non trouvée"));
            }

            // Check if already applied
            if (candidatureRepository.findByCandidatIdAndOffreId(candidatId, id).isPresent()) {
                return ResponseEntity.status(400)
                    .body(Map.of("error", "Vous avez déjà postulé à cette offre"));
            }

            // Create candidature with existing CV
            Candidature candidature = new Candidature();
            candidature.setCandidat(candidat.get());
            candidature.setOffre(offre.get());
            candidature.setCv(existingCV.get());
            candidature.setStatut(StatutCandidature.EN_ATTENTE);
            candidatureRepository.save(candidature);

            return ResponseEntity.ok()
                .body(Map.of("message", "Candidature soumise avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/cv")
    public ResponseEntity<?> uploadCv(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            Long candidatId = getCandidatIdFromRequest(request);

            // Find the candidate
            Optional<Candidat> candidatOpt = candidatRepository.findById(candidatId);
            if (candidatOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "Candidat non trouvé"));
            }
            Candidat candidat = candidatOpt.get();

            // Create and save the CV entity
            CV cv = new CV();
            cv.setCandidat(candidat);
            cv.setNomFichier(file.getOriginalFilename());
           
            cv.setDateUpload(LocalDateTime.now());

            cv.setEstParDefaut(true); // Set as default CV
            cvRepository.save(cv);

            return ResponseEntity.ok(Map.of("message", "CV ajouté avec succès"));
        } catch (Exception e) {
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
        Object idObj = claims.get("id");
        if (idObj == null) throw new Exception("ID manquant dans le token");
        return ((Number) idObj).longValue();
    }
}