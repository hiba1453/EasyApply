package com.easyapply.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easyapply.entity.Job;
import com.easyapply.entity.Profile;
import com.easyapply.entity.Recommendation;
import com.easyapply.entity.User;
import com.easyapply.repository.JobRepository;
import com.easyapply.repository.RecommendationRepository;
import com.easyapply.repository.UserRepository;

@Service
@Transactional
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JobRepository jobRepository;

    // ✅ RÉCUPÉRER LES RECOMMANDATIONS POUR UN UTILISATEUR
    public List<Map<String, Object>> getRecommendationsForUser(Long userId) {
        List<Recommendation> recommendations = recommendationRepository.findByUser_IdOrderByMatchScoreDesc(userId);
        
        return recommendations.stream()
            .map(this::convertRecommendationToMap)
            .collect(Collectors.toList());
    }

    // ✅ GÉNÉRER DE NOUVELLES RECOMMANDATIONS POUR UN UTILISATEUR
    public List<Recommendation> generateRecommendationsForUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        Profile profile = user.getProfile();
        if (profile == null) {
            return Collections.emptyList();
        }

        // Récupérer les offres actives
        List<Job> activeJobs = jobRepository.findByStatus(Job.JobStatus.ACTIVE);
        
        // Supprimer les anciennes recommandations de l'utilisateur
        recommendationRepository.deleteByUser_Id(userId);
        
        List<Recommendation> newRecommendations = new ArrayList<>();
        
        for (Job job : activeJobs) {
            // Calculer le score de correspondance
            double matchScore = calculateMatchScore(profile, job);
            
            if (matchScore > 0.3) { // Seuil minimum de correspondance
                Recommendation recommendation = new Recommendation(user, job, matchScore);
                recommendation.setReasoning(generateReasoning(profile, job, matchScore));
                recommendation.setIsViewed(false);
                
                newRecommendations.add(recommendation);
            }
        }
        
        // Sauvegarder les nouvelles recommandations
        return recommendationRepository.saveAll(newRecommendations);
    }

    // ✅ MARQUER UNE RECOMMANDATION COMME VUE
    public Recommendation markAsViewed(Long recommendationId) {
        Recommendation recommendation = recommendationRepository.findById(recommendationId)
            .orElseThrow(() -> new RuntimeException("Recommandation non trouvée"));
        
        recommendation.setIsViewed(true);
        return recommendationRepository.save(recommendation);
    }

    // ✅ CALCULER LE SCORE DE CORRESPONDANCE ENTRE PROFIL ET OFFRE
    private double calculateMatchScore(Profile profile, Job job) {
        double score = 0.0;
        int factors = 0;
        
        // 1. Correspondance des compétences (40% du score)
        if (profile.getSkills() != null && job.getRequirements() != null) {
            double skillsMatch = calculateSkillsMatch(profile.getSkills(), job.getRequirements());
            score += skillsMatch * 0.4;
            factors++;
        }
        
        // 2. Correspondance de localisation (20% du score)
        if (profile.getPreferredLocation() != null && job.getLocation() != null) {
            double locationMatch = calculateLocationMatch(profile.getPreferredLocation(), job.getLocation());
            score += locationMatch * 0.2;
            factors++;
        }
        
        // 3. Correspondance salariale (20% du score)
        if (profile.getPreferredSalary() != null && job.getSalary() != null) {
            double salaryMatch = calculateSalaryMatch(profile.getPreferredSalary(), job.getSalary().doubleValue());
            score += salaryMatch * 0.2;
            factors++;
        }
        
        // 4. Expérience requise vs expérience du candidat (20% du score)
        if (profile.getYearsOfExperience() != null) {
            double experienceMatch = calculateExperienceMatch(profile.getYearsOfExperience(), job.getRequirements());
            score += experienceMatch * 0.2;
            factors++;
        }
        
        return factors > 0 ? score : 0.0;
    }

    private double calculateSkillsMatch(String userSkills, String jobRequirements) {
        String[] userSkillsArray = userSkills.toLowerCase().split("[,;]");
        String jobRequirementsLower = jobRequirements.toLowerCase();
        
        int matchingSkills = 0;
        for (String skill : userSkillsArray) {
            String trimmedSkill = skill.trim();
            if (trimmedSkill.length() > 2 && jobRequirementsLower.contains(trimmedSkill)) {
                matchingSkills++;
            }
        }
        
        return Math.min(1.0, (double) matchingSkills / Math.max(1, userSkillsArray.length));
    }

    private double calculateLocationMatch(String preferredLocation, String jobLocation) {
        String prefLocationLower = preferredLocation.toLowerCase();
        String jobLocationLower = jobLocation.toLowerCase();
        
        if (prefLocationLower.contains("remote") || jobLocationLower.contains("remote")) {
            return prefLocationLower.contains("remote") && jobLocationLower.contains("remote") ? 1.0 : 0.8;
        }
        
        // Extraire les villes principales
        String[] prefCities = prefLocationLower.split("[,;]");
        String[] jobCities = jobLocationLower.split("[,;]");
        
        for (String prefCity : prefCities) {
            for (String jobCity : jobCities) {
                if (prefCity.trim().contains(jobCity.trim()) || jobCity.trim().contains(prefCity.trim())) {
                    return 1.0;
                }
            }
        }
        
        return 0.3; // Match partiel pour même pays/région
    }

    private double calculateSalaryMatch(Double preferredSalary, Double jobSalary) {
        if (jobSalary >= preferredSalary) {
            return 1.0; // Salaire égal ou supérieur = match parfait
        } else {
            double ratio = jobSalary / preferredSalary;
            return Math.max(0, ratio); // Score proportionnel
        }
    }

    private double calculateExperienceMatch(Integer userExperience, String jobRequirements) {
        // Extraire les années d'expérience requises du texte
        String requirementsLower = jobRequirements.toLowerCase();
        
        // Patterns pour détecter l'expérience requise
        if (requirementsLower.contains("débutant") || requirementsLower.contains("junior")) {
            return userExperience >= 0 ? 1.0 : 0.5;
        } else if (requirementsLower.contains("senior")) {
            return userExperience >= 5 ? 1.0 : Math.max(0.3, (double) userExperience / 5);
        } else if (requirementsLower.contains("5") && requirementsLower.contains("an")) {
            return userExperience >= 5 ? 1.0 : Math.max(0.3, (double) userExperience / 5);
        } else if (requirementsLower.contains("3") && requirementsLower.contains("an")) {
            return userExperience >= 3 ? 1.0 : Math.max(0.4, (double) userExperience / 3);
        } else if (requirementsLower.contains("2") && requirementsLower.contains("an")) {
            return userExperience >= 2 ? 1.0 : Math.max(0.5, (double) userExperience / 2);
        }
        
        return 0.7; // Score par défaut si pas d'info sur l'expérience requise
    }

    private String generateReasoning(Profile profile, Job job, double matchScore) {
        StringBuilder reasoning = new StringBuilder();
        
        reasoning.append("Correspondance de ").append(Math.round(matchScore * 100)).append("% basée sur ");
        
        List<String> factors = new ArrayList<>();
        
        if (profile.getSkills() != null && job.getRequirements() != null) {
            factors.add("vos compétences techniques");
        }
        
        if (profile.getPreferredLocation() != null && job.getLocation() != null) {
            factors.add("votre localisation préférée");
        }
        
        if (profile.getYearsOfExperience() != null) {
            factors.add("votre niveau d'expérience (" + profile.getYearsOfExperience() + " ans)");
        }
        
        if (factors.isEmpty()) {
            reasoning.append("votre profil général");
        } else {
            reasoning.append(String.join(", ", factors));
        }
        
        return reasoning.toString();
    }

    private Map<String, Object> convertRecommendationToMap(Recommendation recommendation) {
        Map<String, Object> recMap = new HashMap<>();
        recMap.put("id", recommendation.getId());
        recMap.put("jobId", recommendation.getJob().getId());
        recMap.put("jobTitle", recommendation.getJob().getTitle());
        recMap.put("company", recommendation.getJob().getCompany());
        recMap.put("location", recommendation.getJob().getLocation());
        recMap.put("salary", recommendation.getJob().getSalary());
        recMap.put("contractType", recommendation.getJob().getContractType());
        recMap.put("matchScore", recommendation.getMatchScore());
        recMap.put("reasoning", recommendation.getReasoning());
        recMap.put("isViewed", recommendation.getIsViewed());
        recMap.put("createdAt", recommendation.getCreatedAt());
        return recMap;
    }

    // ✅ STATISTIQUES DES RECOMMANDATIONS
    public Map<String, Object> getRecommendationStats(Long userId) {
        List<Recommendation> recommendations = recommendationRepository.findByUser_Id(userId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", recommendations.size());
        stats.put("viewed", recommendations.stream().filter(Recommendation::getIsViewed).count());
        stats.put("notViewed", recommendations.stream().filter(r -> !r.getIsViewed()).count());
        
        OptionalDouble avgScore = recommendations.stream().mapToDouble(Recommendation::getMatchScore).average();
        stats.put("averageMatchScore", avgScore.orElse(0.0));
        
        return stats;
    }
}