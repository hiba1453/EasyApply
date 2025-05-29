package com.easyapply.service;

import com.easyapply.entity.User;
import com.easyapply.entity.Profile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.HashMap;

@Service
public class LinkedInService {

    @Value("${spring.security.oauth2.client.registration.linkedin.client-id}")
    private String clientId;
    
    @Value("${spring.security.oauth2.client.registration.linkedin.client-secret}")
    private String clientSecret;
    
    private final RestTemplate restTemplate = new RestTemplate();

    // ✅ RÉCUPÉRER PROFIL LINKEDIN COMPLET
    public Map<String, Object> getLinkedInProfile(String accessToken) {
        try {
            // URL de l'API LinkedIn v2
            String profileUrl = "https://api.linkedin.com/v2/people/~" +
                "?projection=(id,firstName,lastName,headline,summary,positions,educations,skills)";
            
            // Headers avec token d'accès
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            // Appel API LinkedIn
            ResponseEntity<Map> response = restTemplate.exchange(
                profileUrl, 
                HttpMethod.GET, 
                entity, 
                Map.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
            
            throw new RuntimeException("Erreur lors de la récupération du profil LinkedIn");
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur LinkedIn API: " + e.getMessage());
        }
    }

    // ✅ EXTRAIRE DONNÉES PROFIL POUR USER
    public User createUserFromLinkedIn(Map<String, Object> linkedInData) {
        User user = new User();
        
        // Informations de base
        if (linkedInData.containsKey("firstName")) {
            Map<String, Object> firstName = (Map<String, Object>) linkedInData.get("firstName");
            Map<String, Object> localized = (Map<String, Object>) firstName.get("localized");
            if (localized != null && !localized.isEmpty()) {
                user.setFirstName(localized.values().iterator().next().toString());
            }
        }
        
        if (linkedInData.containsKey("lastName")) {
            Map<String, Object> lastName = (Map<String, Object>) linkedInData.get("lastName");
            Map<String, Object> localized = (Map<String, Object>) lastName.get("localized");
            if (localized != null && !localized.isEmpty()) {
                user.setLastName(localized.values().iterator().next().toString());
            }
        }
        
        // ID LinkedIn comme token
        if (linkedInData.containsKey("id")) {
            user.setLinkedinToken(linkedInData.get("id").toString());
        }
        
        return user;
    }

    // ✅ EXTRAIRE DONNÉES PROFESSIONNEL POUR PROFILE
    public Profile createProfileFromLinkedIn(Map<String, Object> linkedInData, User user) {
        Profile profile = new Profile(user);
        
        // Résumé professionnel
        if (linkedInData.containsKey("headline")) {
            profile.setSummary(linkedInData.get("headline").toString());
        }
        
        // Expériences professionnelles
        if (linkedInData.containsKey("positions")) {
            Map<String, Object> positions = (Map<String, Object>) linkedInData.get("positions");
            if (positions.containsKey("elements")) {
                profile.setExperiences(extractExperiences(positions));
            }
        }
        
        // Formations
        if (linkedInData.containsKey("educations")) {
            Map<String, Object> educations = (Map<String, Object>) linkedInData.get("educations");
            if (educations.containsKey("elements")) {
                profile.setFormations(extractEducations(educations));
            }
        }
        
        // Compétences
        if (linkedInData.containsKey("skills")) {
            Map<String, Object> skills = (Map<String, Object>) linkedInData.get("skills");
            if (skills.containsKey("elements")) {
                profile.setSkills(extractSkills(skills));
            }
        }
        
        // Analyser le profil pour calculer la complétude
        profile.analyserProfil();
        
        return profile;
    }

    // ✅ MÉTHODES D'EXTRACTION DÉTAILLÉES
    private String extractExperiences(Map<String, Object> positions) {
        StringBuilder experiences = new StringBuilder();
        
        try {
            Object elements = positions.get("elements");
            if (elements instanceof java.util.List) {
                java.util.List<Map<String, Object>> positionList = (java.util.List<Map<String, Object>>) elements;
                
                for (Map<String, Object> position : positionList) {
                    if (position.containsKey("title")) {
                        experiences.append("• ").append(position.get("title"));
                    }
                    if (position.containsKey("companyName")) {
                        experiences.append(" chez ").append(position.get("companyName"));
                    }
                    experiences.append("\n");
                }
            }
        } catch (Exception e) {
            // Log error mais continue
            System.err.println("Erreur extraction expériences: " + e.getMessage());
        }
        
        return experiences.toString();
    }
    
    private String extractEducations(Map<String, Object> educations) {
        StringBuilder formations = new StringBuilder();
        
        try {
            Object elements = educations.get("elements");
            if (elements instanceof java.util.List) {
                java.util.List<Map<String, Object>> educationList = (java.util.List<Map<String, Object>>) elements;
                
                for (Map<String, Object> education : educationList) {
                    if (education.containsKey("schoolName")) {
                        formations.append("• ").append(education.get("schoolName"));
                    }
                    if (education.containsKey("degreeName")) {
                        formations.append(" - ").append(education.get("degreeName"));
                    }
                    formations.append("\n");
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur extraction formations: " + e.getMessage());
        }
        
        return formations.toString();
    }
    
    private String extractSkills(Map<String, Object> skills) {
        StringBuilder competences = new StringBuilder();
        
        try {
            Object elements = skills.get("elements");
            if (elements instanceof java.util.List) {
                java.util.List<Map<String, Object>> skillList = (java.util.List<Map<String, Object>>) elements;
                
                for (Map<String, Object> skill : skillList) {
                    if (skill.containsKey("name")) {
                        if (competences.length() > 0) competences.append(", ");
                        competences.append(skill.get("name"));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur extraction compétences: " + e.getMessage());
        }
        
        return competences.toString();
    }

    // ✅ GÉNÉRER RECOMMANDATIONS BASÉES SUR PROFIL LINKEDIN
    public Map<String, Object> generateRecommendationsFromProfile(Profile profile) {
        Map<String, Object> recommendations = new HashMap<>();
        
        // Analyse des compétences pour matching
        String skills = profile.getSkills();
        if (skills != null && !skills.isEmpty()) {
            // Logique de matching basique basée sur les compétences
            double javaScore = skills.toLowerCase().contains("java") ? 0.9 : 0.0;
            double pythonScore = skills.toLowerCase().contains("python") ? 0.85 : 0.0;
            double reactScore = skills.toLowerCase().contains("react") ? 0.8 : 0.0;
            
            recommendations.put("matching_scores", Map.of(
                "java_jobs", javaScore,
                "python_jobs", pythonScore,
                "react_jobs", reactScore
            ));
        }
        
        // Analyse de l'expérience
        String experience = profile.getExperiences();
        if (experience != null && !experience.isEmpty()) {
            int seniorityLevel = calculateSeniorityLevel(experience);
            recommendations.put("seniority_level", seniorityLevel);
        }
        
        return recommendations;
    }
    
    private int calculateSeniorityLevel(String experience) {
        // Logique basique pour déterminer le niveau de séniorité
        long experienceCount = experience.toLowerCase().split("•").length - 1;
        
        if (experienceCount >= 5) return 3; // Senior
        if (experienceCount >= 2) return 2; // Mid-level  
        return 1; // Junior
    }
}