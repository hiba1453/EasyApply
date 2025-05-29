package com.easyapply.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easyapply.entity.Entreprise;
import com.easyapply.repository.EntrepriseRepository;

@Service
@Transactional
public class EntrepriseService {

    @Autowired
    private EntrepriseRepository entrepriseRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ GESTION DES ENTREPRISES
    public List<Entreprise> getAllEntreprises() {
        return entrepriseRepository.findAll();
    }

    public Optional<Entreprise> getEntrepriseById(Long id) {
        return entrepriseRepository.findById(id);
    }

    public Optional<Entreprise> getEntrepriseByEmail(String email) {
        return entrepriseRepository.findByEmail(email);
    }

    // ✅ INSCRIPTION ENTREPRISE
    public Entreprise createEntreprise(String email, String password, String nom, 
                                     String secteur, String siteWeb, String adresse, 
                                     String numeroSiret, String description) {
        // Vérifier si l'email existe déjà
        if (entrepriseRepository.existsByEmail(email)) {
            throw new RuntimeException("Email déjà utilisé");
        }

        // Vérifier SIRET si fourni
        if (numeroSiret != null && !numeroSiret.isEmpty() && 
            entrepriseRepository.existsByNumeroSiret(numeroSiret)) {
            throw new RuntimeException("Numéro SIRET déjà utilisé");
        }

        // Créer la nouvelle entreprise
        Entreprise entreprise = new Entreprise();
        entreprise.setEmail(email);
        entreprise.setPassword(passwordEncoder.encode(password));
        entreprise.setNom(nom);
        entreprise.setSecteur(secteur);
        entreprise.setSiteWeb(siteWeb);
        entreprise.setAdresse(adresse);
        entreprise.setNumeroSiret(numeroSiret);
        entreprise.setDescription(description);
        entreprise.setIsActive(true);

        return entrepriseRepository.save(entreprise);
    }

    // ✅ MISE À JOUR ENTREPRISE
    public Entreprise updateEntreprise(Long entrepriseId, Map<String, Object> updates) {
        Entreprise entreprise = entrepriseRepository.findById(entrepriseId)
            .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));

        if (updates.containsKey("nom")) {
            entreprise.setNom((String) updates.get("nom"));
        }
        if (updates.containsKey("secteur")) {
            entreprise.setSecteur((String) updates.get("secteur"));
        }
        if (updates.containsKey("siteWeb")) {
            entreprise.setSiteWeb((String) updates.get("siteWeb"));
        }
        if (updates.containsKey("adresse")) {
            entreprise.setAdresse((String) updates.get("adresse"));
        }
        if (updates.containsKey("description")) {
            entreprise.setDescription((String) updates.get("description"));
        }

        return entrepriseRepository.save(entreprise);
    }

    // ✅ RECHERCHE ENTREPRISES
    public List<Entreprise> searchEntreprisesByName(String nom) {
        return entrepriseRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Entreprise> getEntreprisesBySecteur(String secteur) {
        return entrepriseRepository.findBySecteurContainingIgnoreCase(secteur);
    }

    public List<Entreprise> getActiveEntreprises() {
        return entrepriseRepository.findByIsActiveTrue();
    }

    public List<Entreprise> getEntreprisesWithActiveJobs() {
        return entrepriseRepository.findEntreprisesWithActiveJobs();
    }

    // ✅ ENTREPRISES RÉCENTES - IMPLÉMENTATION ALTERNATIVE
    public List<Entreprise> getRecentEntreprises() {
        // Utiliser findAll() et filtrer en Java pour éviter les problèmes HQL
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return entrepriseRepository.findAll().stream()
            .filter(e -> e.getCreatedAt().isAfter(thirtyDaysAgo))
            .sorted((e1, e2) -> e2.getCreatedAt().compareTo(e1.getCreatedAt()))
            .toList();
    }

    // ✅ DÉSACTIVATION/ACTIVATION
    public Entreprise deactivateEntreprise(Long entrepriseId) {
        Entreprise entreprise = entrepriseRepository.findById(entrepriseId)
            .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));
        
        entreprise.setIsActive(false);
        return entrepriseRepository.save(entreprise);
    }

    public Entreprise activateEntreprise(Long entrepriseId) {
        Entreprise entreprise = entrepriseRepository.findById(entrepriseId)
            .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));
        
        entreprise.setIsActive(true);
        return entrepriseRepository.save(entreprise);
    }

    // ✅ STATISTIQUES ENTREPRISE
    public Map<String, Object> getEntrepriseStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalEntreprises", entrepriseRepository.count());
        stats.put("activeEntreprises", entrepriseRepository.countActiveEntreprises());
        
        // Statistiques par secteur
        List<Object[]> secteurStats = entrepriseRepository.getEntreprisesBySecteur();
        Map<String, Long> secteurMap = new HashMap<>();
        for (Object[] stat : secteurStats) {
            secteurMap.put((String) stat[0], (Long) stat[1]);
        }
        stats.put("entreprisesBySecteur", secteurMap);
        
        return stats;
    }

    // ✅ SUPPRESSION ENTREPRISE
    public void deleteEntreprise(Long entrepriseId) {
        Entreprise entreprise = entrepriseRepository.findById(entrepriseId)
            .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));
        
        // Vérifier s'il y a des offres d'emploi
        if (!entreprise.getJobs().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer une entreprise ayant des offres d'emploi");
        }
        
        entrepriseRepository.deleteById(entrepriseId);
    }
}